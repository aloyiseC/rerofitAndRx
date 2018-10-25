package laiwei.rxandretrofit.bean;

import java.util.ArrayList;

/**
 * Created by laiwei on 2017/10/12 0012.
 */
public class QuestionTypeList implements DataConverter{

    private ArrayList<QuestionType> list;

    public void setList(ArrayList<QuestionType> list) {
        this.list = list;
    }

    public ArrayList<QuestionType> getList() {
        return list;
    }

    @Override
    public Object getData() {
        return list;
    }
}
