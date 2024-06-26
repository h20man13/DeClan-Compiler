package io.github.H20man13.DeClan.common.pat;

import java.lang.reflect.InaccessibleObjectException;

import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.exp.Exp;

public abstract class P {
    private static class IADD extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IADD){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "IADD";
        }
    }

    private static class RADD extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RADD){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "RADD";
        }
    }
    private static class ISUB extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ISUB){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "ISUB";
        }
        
    }

    private static class RSUB extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RSUB){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "RSUB";
        }
        
    }
    private static class IMUL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IMUL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "IMUL";
        }
        
    }

    private static class RMUL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RMUL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "RMUL";
        }
    }

    private static class IDIV extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IDIV){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "IDIV";
        }
    }

    private static class RDIVIDE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RDIVIDE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "RDIVIDE";
        }
    }
    private static class IMOD extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IMOD){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "IMOD";
        }
    }
    public static class GE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof GE){
                return true;
            } else {
                return false;
            }
        }
        @Override
        public String toString(){
            return "GE";
        }
    }
    public static class GT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof GT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "GT";
        }
    }
    public static class LT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof LT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "LT";
        }
    }
    public static class LE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof LE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "LE";
        }
    }
    public static class NE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof NE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "NE";
        }
    }
    public static class EQ extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof EQ){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "EQ";
        }
    }
    public static class ASSIGN extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ASSIGN){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "ASSIGN";
        }  
    }
    public static class INEG extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof INEG){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "INEG";
        }
    }

    public static class RNEG extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RNEG){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "RNEG";
        }
    }
    public static class BNOT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof BNOT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "BNOT";
        }
    }
    public static class BAND extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof BAND){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "BAND";
        }
    }
    public static class BOR extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof BOR){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "BOR";
        }
    }

    public static class IAND extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IAND){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "IAND";
        }
    }

    public static class IOR extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IOR){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "IOR";
        }
    }

    public static class IXOR extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IXOR){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "IXOR";
        }
    }

    public static class LSHIFT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof LSHIFT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "LSHIFT";
        }
        
    }

    public static class RSHIFT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RSHIFT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "RSHIFT";
        }
        
    }

    public static class INOT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof INOT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "INOT";
        }
    }
    public static class IF extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IF){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "IF";
        }
        
    }
    public static class BOOL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof BOOL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "BOOL";
        }
    }
    public static class INT extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof INT){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "INT";
        }
        
    }
    public static class REAL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof REAL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "REAL";
        }
        
    }
    public static class STR extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof STR){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "STR";
        }
    }
    public static class ID extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ID){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "ID";
        } 
    }
    public static class THEN extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof THEN){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "THEN";
        }
        
    }
    public static class ELSE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ELSE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "ELSE";
        }  
    }
    public static class END extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof END){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "END";
        }
    }
    public static class LABEL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof LABEL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "LABEL";
        } 
    }
    public static class GOTO extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof GOTO){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "GOTO";
        }
    }
    public static class RETURN extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RETURN){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "RETURN";
        }
        
    }
    public static class PROC extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PROC){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "PROC";
        }
        
    }
    public static class CALL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CALL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "CALL";
        }
    }

    public static class INLINE extends P{

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof INLINE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "INLINE";
        }
        
    }

    public static class PARAM extends P{

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PARAM){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "PARAM";
        }
    }

    public static class INTERNAL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof INTERNAL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "INTERNAL";
        }
        
    }

    public static class EXTERNAL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof EXTERNAL){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString(){
            return "EXTERNAL";
        }
    }

    public static class PLACE extends P{

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PLACE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "PLACE";
        }
        
    }

    public static class PUBLIC extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PUBLIC){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "PUBLIC";
        }
    }

    public static class PRIVATE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PRIVATE){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "PRIVATE";
        }
    }

    public static class CONST extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CONST){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "CONST";
        }
    }

    public static class DEF extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof DEF){
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "DEF";
        }
    }

    public static class GLOBAL extends P{

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof GLOBAL)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "GLOBAL";
        }
        
    }

    public static class ARGUMENT extends P{

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ARGUMENT)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "ARGUMENT";
        }
    }

    public static class SECTION extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SECTION)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "SECTION";
        }
    }

    public static class BSS extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof BSS)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "BSS";
        }
    }

    public static class DATA extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof DATA)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "DATA";
        }
    }

    public static class CODE extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CODE)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "CODE";
        }
    }

    public static class SYMBOL extends P{
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SYMBOL)
                return true;
            else
                return false;
        }

        @Override
        public String toString() {
            return "SYMBOL";
        }
    }

    public static class PAT extends P{
        private P[] pattern;

        private PAT(P... pattern){
            this.pattern = pattern;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PAT){
                PAT other = (PAT)obj;
                if(other.pattern.length == pattern.length){
                    for(int i = 0; i < pattern.length; i++){
                        P otherElem = other.pattern[i];
                        P thisElem = this.pattern[i];
                        if(!thisElem.equals(otherElem)){
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public int hashCode(){
            int hashCode = 0;
            
            for(P pat : pattern){
                hashCode += pat.hashCode();
            }

            return hashCode;
        }

        @Override
        public String toString(){
            StringBuilder builder = new StringBuilder();
            builder.append(" PAT");
            builder.append('(');

            for(int i = 0; i < pattern.length; i++){
                builder.append(pattern[i].toString());
                if(i < pattern.length - 1){
                    builder.append(", ");
                }
            }
            builder.append(") ");

            return builder.toString();
        }
    }

    public static IADD IADD(){
        return new IADD();
    }

    public static RADD RADD(){
        return new RADD();
    }

    public static ISUB ISUB(){
        return new ISUB();
    }

    public static RSUB RSUB(){
        return new RSUB();
    }

    public static IMUL IMUL(){
        return new IMUL();
    }

    public static RMUL RMUL(){
        return new RMUL();
    }

    public static IDIV IDIV(){
        return new IDIV();
    }

    public static RDIVIDE RDIVIDE(){
        return new RDIVIDE();
    }
    
    public static IMOD IMOD(){
        return new IMOD();
    }
    public static GE GE(){
        return new GE();
    }
    public static GT GT(){
        return new GT();
    }
    public static LT LT(){
        return new LT();
    }
    public static LE LE(){
        return new LE();
    }
    public static NE NE(){
        return new NE();
    }
    public static EQ EQ(){
        return new EQ();
    }
    public static ASSIGN ASSIGN(){
        return new ASSIGN();
    }
    public static INEG INEG(){
        return new INEG();
    }
    public static RNEG RNEG(){
        return new RNEG();
    }
    
    public static BNOT BNOT(){
        return new BNOT();
    }
    public static INOT INOT(){
        return new INOT();
    }
    public static BAND BAND(){
        return new BAND();
    }
    public static IAND IAND(){
        return new IAND();
    }
    public static BOR BOR(){
        return new BOR();
    }
    public static IOR IOR(){
        return new IOR();
    }
    public static IXOR IXOR(){
        return new IXOR();
    }
    public static LSHIFT LSHIFT(){
        return new LSHIFT();
    }
    public static RSHIFT RSHIFT(){
        return new RSHIFT();
    }
    public static IF IF(){
        return new IF();
    }
    public static BOOL BOOL(){
        return new BOOL();
    }
    public static INT INT(){
        return new INT();
    }
    public static REAL REAL(){
        return new REAL();
    }
    public static STR STR(){
        return new STR();
    }
    public static ID ID(){
        return new ID();
    }
    public static THEN THEN(){
        return new THEN();
    }
    public static ELSE ELSE(){
        return new ELSE();
    }
    public static END END(){
        return new END();
    }
    public static LABEL LABEL(){
        return new LABEL();
    }
    public static GOTO GOTO(){
        return new GOTO();
    }
    public static RETURN RETURN(){
        return new RETURN();
    }
    public static PROC PROC(){
        return new PROC();
    }
    public static CALL CALL(){
        return new CALL();
    }
    public static INLINE INLINE(){
        return new INLINE();
    }
    public static PAT PAT(P... inputs){
        return new PAT(inputs);
    }
    public static PARAM PARAM(){
        return new PARAM();
    }
    public static INTERNAL INTERNAL(){
        return new INTERNAL();
    }
    public static EXTERNAL EXTERNAL(){
        return new EXTERNAL();
    }
    public static PLACE PLACE(){
        return new PLACE();
    }
    public static CONST CONST(){
        return new CONST();
    }
    public static PUBLIC PUBLIC(){
        return new PUBLIC();
    }
    public static PRIVATE PRIVATE(){
        return new PRIVATE();
    }
    public static DEF DEF(){
        return new DEF();
    }
    public static GLOBAL GLOBAL(){
        return new GLOBAL();
    }
    public static ARGUMENT ARGUMENT(){
        return new ARGUMENT();
    }
    public static BSS BSS(){
        return new BSS();
    }
    public static DATA DATA(){
        return new DATA();
    }
    public static SYMBOL SYMBOL(){
        return new SYMBOL();
    }
    public static CODE CODE(){
        return new CODE();
    }
    public static SECTION SECTION(){
        return new SECTION();
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract String toString();

    @Override
    public int hashCode(){
        return 1;
    }
}
