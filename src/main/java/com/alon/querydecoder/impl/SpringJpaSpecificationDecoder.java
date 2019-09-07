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
    
    public Predicate decode(Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (this.decoder instanceof Group)
            return this.decode((Group) this.decoder, root, criteriaBuilder);
        
        return this.decode((Expression) this.decoder, root, criteriaBuilder);
    }
    
    private Predicate decode(Group group, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = this.decode(group.getDecoder(), root, criteriaBuilder);
        return this.decodeNext(predicate, group, root, criteriaBuilder);
    }
    
    private Predicate decode(Expression expression, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate predicate;
        Path path = this.getPath(root, new ArrayList<>(Arrays.asList(expression.getField().split("\\."))));
        
        switch (expression.getMatchType()) {
            case BT :
                String[] values = expression.getValue().split("-");
                
            	if (path.getJavaType().equals(Long.class))
                    predicate = criteriaBuilder.between(path, Long.valueOf(values[0]), Long.valueOf(values[1]));
            	else
                    predicate = criteriaBuilder.between(path, values[0], values[1]);
            		 
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
        
        return this.decodeNext(predicate, expression, root, criteriaBuilder);
    }
    
    private Predicate decode(Decoder decoder, Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (decoder instanceof Group)
            return this.decode((Group) decoder, root, criteriaBuilder);
        
        return this.decode((Expression) decoder, root, criteriaBuilder);
    }
    
    private Predicate decodeNext(Predicate predicate, Decoder decoder, Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (decoder.getNext() == null)
            return predicate;
        
        Predicate nextPredicate = this.decode(decoder.getNext(), root, criteriaBuilder);

        if (decoder.getLogicalOperator().equals(LogicalOperator.AND))
            return criteriaBuilder.and(predicate, nextPredicate);

        return criteriaBuilder.or(predicate, nextPredicate);
    }
    
    private Path<?> getPath(Path<?> parent, List<String> props) {
        Path<?> p = parent.get(props.remove(0));

        if (!props.isEmpty())
            return getPath(p, props);

        return p;
    }
    
}
