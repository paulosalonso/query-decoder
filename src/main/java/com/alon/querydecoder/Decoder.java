package com.alon.querydecoder;

public interface Decoder {
    
    public LogicalOperator getLogicalOperator();
    public Decoder getNext();
    public <D extends Decoder> D parse(String query);

}