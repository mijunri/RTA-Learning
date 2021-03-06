package rta;

import words.TimeWord;

public class TimeGuard{

    public static final int MAX_TIME = 1000;

    private boolean leftOpen;
    private boolean rightOpen;
    private int left;
    private int right;

    public TimeGuard(){}

    public TimeGuard(String pattern){
        pattern = pattern.trim();
        if(pattern.charAt(0) == '['){
            setLeftOpen(false);
        }else if(pattern.charAt(0) == '('){
            setLeftOpen(true);
        }else {
            throw new RuntimeException("guard pattern error");
        }
        int size = pattern.length();
        if(pattern.charAt(size-1) == ']'){
            setRightOpen(false);
        }else if(pattern.charAt(size-1) == ')'){
            setRightOpen(true);
        }else {
            throw new RuntimeException("guard pattern error");
        }
        String[] numbers = pattern.split("\\,|\\[|\\(|\\]|\\)");
        int left = Integer.parseInt(numbers[1]);
        int right;
        if(numbers[2].equals("+")){
            right = MAX_TIME;
        }else {
            right = Integer.parseInt(numbers[2]);
        }
        setLeft(left);
        setRight(right);
    }

    public TimeGuard(boolean leftOpen, boolean rightOpen, int left, int right) {
        this.leftOpen = leftOpen;
        this.rightOpen = rightOpen;
        this.left = left;
        this.right = right;
    }



    public TimeGuard(TimeWord timeWord){
        double time = timeWord.getValue();
        if(time == (int)time){
            this.leftOpen = false;
            this.rightOpen = false;
            this.left = (int)time;
            this.right = (int)time;
        }
        else {
            this.leftOpen = true;
            this.rightOpen = true;
            this.left = (int)time;
            this.right = (int)time+1;
        }
    }

    public boolean isLeftOpen() {
        return leftOpen;
    }

    public void setLeftOpen(boolean leftOpen) {
        this.leftOpen = leftOpen;
    }

    public boolean isRightOpen() {
        return rightOpen;
    }

    public void setRightOpen(boolean rightOpen) {
        this.rightOpen = rightOpen;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }


    public boolean isPass(double value){

        if(leftOpen && rightOpen){
            if(value > left && value < right){
                return true;
            }
            return false;
        }
        if(!leftOpen && rightOpen){
            if(value >= left && value < right){
                return true;
            }
            return false;
        }
        if(leftOpen && !rightOpen){
            if(value > left && value <= right){
                return true;
            }
            return false;
        }
        if(!leftOpen && !rightOpen){
            if(value >= left && value <= right){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        if(leftOpen){
            stringBuilder.append("(");
        }
        else {
            stringBuilder.append("[");
        }
        stringBuilder.append(left).append(",").append(right);
        if(rightOpen){
            stringBuilder.append(")");
        }else {
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    public TimeGuard copy(){
        return new TimeGuard(leftOpen,rightOpen,left,right);
    }

    public String toExpression(){
        StringBuilder stringBuilder = new StringBuilder();
        if(leftOpen){
            stringBuilder.append("x>"+left);
        }else {
            stringBuilder.append("x >="+left);
        }
        stringBuilder.append(" && ");
        if(rightOpen){
            stringBuilder.append("x<"+right);
        }else {
            stringBuilder.append("x<="+right);
        }
        return stringBuilder.toString();
    }

    //求和另一个timeGuard的交集
    public TimeGuard intersection(TimeGuard timeGuard){
        int l,r;
        boolean lo,ro;
        if(left < timeGuard.left){
            l = timeGuard.left;
            lo = timeGuard.leftOpen;
        }else if(left == timeGuard.left){
            l = timeGuard.left;
            lo = leftOpen || timeGuard.leftOpen;
        }else {
            l = left;
            lo = leftOpen;
        }

        if(right > timeGuard.right){
            r = timeGuard.right;
            ro = timeGuard.rightOpen;
        }else if(right == timeGuard.right){
            r = timeGuard.right;
            ro = rightOpen || timeGuard.rightOpen;
        }else {
            r = right;
            ro = rightOpen;
        }

        if(l > r){
            return null;
        }else if(l == r){
            if(lo || ro){
                return null;
            }else {
                return new TimeGuard(lo,ro,l,r);
            }
        }else {
            return new TimeGuard(lo,ro,l,r);
        }
    }

    @Override
    public int hashCode(){
        return left+right*2;
    }

    @Override
    public boolean equals(Object o){
        TimeGuard timeGuard = (TimeGuard)o;
        if(this.leftOpen==timeGuard.leftOpen && this.left == timeGuard.left &&
            this.right == timeGuard.right && this.rightOpen == timeGuard.rightOpen){
            return true;
        }
        return false;
    }
}
