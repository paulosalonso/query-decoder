/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alon.querydecoder.impl;

import com.alon.querydecoder.Decoder;
import com.alon.querydecoder.Expression;
import com.alon.querydecoder.Group;
import com.alon.querydecoder.LogicalOperator;
import com.alon.querydecoder.QueryDecoder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author paulo
 * @param <T>
 */
public class SpringJpaSpecificationDecoder<T> extends QueryDecoder<Predicate> implements Specification<T> {

    public SpringJpaSpecificationDecoder(String query) {
        super(query, SpringJpaSpecificationDecoder::decode);
    }
    
    private static Predicate decode(Decoder group) {
        throw new UnsupportedOperationException(
                            "This operation is unsupported. " + 
                            "This decoder was implemented to work in conjunction with " + 
                            "org.springframework.data.jpa.repository.JpaSpecificationExecutor methods."
        );
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return this.decode(root, criteriaBuilder);
    }
    
    private Path<?> getAbsolutePath(Path<?> parent, List<String> props) {
        Path<?> p = parent.get(props.remove(0));

        if (!props.isEmpty())
            p = getAbsolutePath(p, props);

        return p;
    }
    
    public Predicate decode(Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (this.decoder instanceof Group)
            return this.decode((Group) this.decoder, root, criteriaBuilder);
        
        return this.decode((Expression) this.decoder, root, criteriaBuilder);
    }
    
    private Predicate decode(
            Group group, 
            Root<T> root,
            CriteriaBuilder criteriaBuilder
    ) {
        Predicate predicate;
        
        if (group.getDecoder() instanceof Group)
            predicate = this.decode((Group) group.getDecoder(), root, criteriaBuilder);
        else
            predicate = this.decode((Expression) group.getDecoder(), root, criteriaBuilder);
        
        if (group.getNext() != null) {
            Predicate nextPredicate;
            
            if (group.getNext() instanceof Group)
                nextPredicate = this.decode((Group) group.getNext(), root, criteriaBuilder);
            else
                nextPredicate = this.decode((Expression) group.getNext(), root, criteriaBuilder);
            
            if (group.getLogicalOperator().equals(LogicalOperator.AND))
                predicate = criteriaBuilder.and(predicate, nextPredicate);
            else
                predicate = criteriaBuilder.or(predicate, nextPredicate);    
        }

        return predicate;
    }
    
    private Predicate decode(
            Expression expression, 
            Root<T> root,
            CriteriaBuilder criteriaBuilder
    ) {
        Predicate predicate;
        Path path = this.getAbsolutePath(root, new ArrayList<>(Arrays.asList(expression.getField().split("\\."))));
        
        switch (expression.getMatchType()) {
            case BT :
            	if (path.getJavaType().equals(Long.class)) {
                        Long v1 = Long.valueOf(expression.getValue().split("-")[0]);
                        Long v2 = Long.valueOf(expression.getValue().split("-")[1]);
                        predicate = criteriaBuilder.between(path, v1, v2);
            	} else
                        predicate = criteriaBuilder.between(path, expression.getValue().split("-")[0], expression.getValue().split("-")[1]);
            		 
            	break;
            case CT : predicate = criteriaBuilder.like(path, "%".concat(expression.getValue()).concat("%")); break;
            case EQ : predicate = criteriaBuilder.equal(path, expression.getValue()); break;
            case GT : predicate = criteriaBuilder.gt(path, new BigDecimal(expression.getValue())); break;
            case GTE: predicate = criteriaBuilder.greaterThanOrEqualTo(path, new BigDecimal(expression.getValue())); break;
            case LT : predicate = criteriaBuilder.le(path, new BigDecimal(expression.getValue())); break;
            case LTE: predicate = criteriaBuilder.lessThanOrEqualTo(path, new BigDecimal(expression.getValue())); break;
            case IN : predicate = path.in(Arrays.asList(expression.getValue().split(","))); break;
            default : predicate = criteriaBuilder.equal(path, expression.getValue());
        }
        
        if (expression.getNext() != null) {
            Predicate nextPredicate;
            
            if (expression.getNext() instanceof Expression)
                nextPredicate = this.decode((Expression) expression.getNext(), root, criteriaBuilder);
            else
                nextPredicate = this.decode((Group) expression.getNext(), root, criteriaBuilder);
                
            if (expression.getLogicalOperator().equals(LogicalOperator.AND))
                predicate = criteriaBuilder.and(predicate, nextPredicate);
            else
                predicate = criteriaBuilder.or(predicate, nextPredicate);
        }
        
        return predicate;
    }
    
}
