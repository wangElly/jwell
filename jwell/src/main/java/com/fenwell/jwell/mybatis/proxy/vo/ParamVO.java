package com.fenwell.jwell.mybatis.proxy.vo;

import java.util.List;

public class ParamVO {

    /**
     * 参数是否为空
     */
    private boolean isEmpty;

    /**
     * 参数是否为单个值
     */
    private boolean isSingle;

    /**
     * 参数的索引
     */
    private String[] value;

    private int[] index;

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

    public void setIndex(List<Integer> index) {
        this.index = new int[index.size()];
        for (int i = 0; i < index.size(); i++) {
            this.index[i] = index.get(i);
        }
    }

    public void setValue(List<String> value) {
        this.value = new String[value.size()];
        for (int i = 0; i < value.size(); i++) {
            this.value[i] = value.get(i);
        }
    }

}
