package com.example.testandroid.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.testandroid.MainApplication;
import com.example.testandroid.bean.CountryMobileCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MobileCodeManager {

    private static MobileCodeManager instance;
    private final List<CountryMobileCode> countryMobileCodeList;
    private List<CountryMobileCode> countryMobileCodeListSearch;
    private final List<String> countryIndexCodeList;
    private final Map<String, Integer> indexList;
    private final Map<String, Integer> indexListSearch;
    private final Executor executor;
    private boolean isLoading;
    private final List<TaskCompleteListener> taskCompleteListenerList;
    private String keyword;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final String priority_keyword = "中国";

    private MobileCodeManager(){
        countryMobileCodeList = new ArrayList<>();
        taskCompleteListenerList = new ArrayList<>();
        countryIndexCodeList = new ArrayList<>();
        countryMobileCodeListSearch = new ArrayList<>();
        indexList = new HashMap<>();
        indexListSearch = new HashMap<>();
        executor = Executors.newSingleThreadExecutor();
        isLoading = false;
    }

    public synchronized static MobileCodeManager getInstance(){
        if (instance == null) {
            instance = new MobileCodeManager();
        }
        return instance;
    }

    public void addListener(TaskCompleteListener taskCompleteListener){
        try {
            if (taskCompleteListenerList != null && !taskCompleteListenerList.contains(taskCompleteListener)) {
                taskCompleteListenerList.add(taskCompleteListener);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeListener(TaskCompleteListener taskCompleteListener){
        try {
            if (taskCompleteListener != null) {
                taskCompleteListenerList.remove(taskCompleteListener);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadMobileCode(){
        if (executor != null) {
            if (isLoading) {
                return;
            }
            isLoading = true;
            executor.execute(new LoadMobileCodeTaskRunnable());
        }
    }

    public void getCountryMobileCodeList(){
        if (countryMobileCodeList != null && !countryMobileCodeList.isEmpty()) {
            if (taskCompleteListenerList.isEmpty()) {
                return;
            }
            for (TaskCompleteListener taskCompleteListener : taskCompleteListenerList){
                taskCompleteListener.updateCountryMobileCodeList(countryMobileCodeList, countryIndexCodeList);
            }
            return;
        }
        loadMobileCode();
    }

    public void search(String keyword){
        this.keyword = keyword;
        if (TextUtils.isEmpty(keyword) || executor == null) {
            notifyUpdateCountryMobileCodeList(countryMobileCodeList);
            return;
        }
        executor.execute(new SearchMobileCodeTaskRunnable());
    }


    private void loadMobileCodeList(){
        loadDataFromXML(MainApplication.getInstance());
    }


    private void loadDataFromXML(Context context) {
        countryMobileCodeList.clear();
        countryIndexCodeList.clear();
        indexList.clear();
        List<CountryMobileCode> priorityList = new ArrayList<>();
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlFactoryObject.newPullParser();
            InputStream ins = context.getResources().openRawResource(context.getResources()
                    .getIdentifier("chinese_simplified", "raw", context.getPackageName()));
            xmlPullParser.setInput(ins, "UTF-8");
            int event = xmlPullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xmlPullParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("country")) {
                            CountryMobileCode countryMobileCode = new CountryMobileCode();
                            countryMobileCode.setName_code(xmlPullParser.getAttributeValue(null, "name_code").toUpperCase());
                            countryMobileCode.setPhone_code("+"+xmlPullParser.getAttributeValue(null, "phone_code"));
                            countryMobileCode.setEnglish_name(xmlPullParser.getAttributeValue(null, "english_name"));
                            countryMobileCode.setName(xmlPullParser.getAttributeValue(null, "name"));
                            countryMobileCode.setMobileCodeType();

                            boolean isMatchKeyword = isMatchKeyword(countryMobileCode,priority_keyword);
                            String index = countryMobileCode.getName_code().substring(0,1);
                            if (!isMatchKeyword && !countryIndexCodeList.contains(index)) {
                                countryIndexCodeList.add(index);
                                CountryMobileCode countryMobileCodeIndex = new CountryMobileCode();
                                countryMobileCodeIndex.setTitleType();
                                countryMobileCodeIndex.setName(index);
                                countryMobileCodeIndex.setName_code(index);
                                countryMobileCodeList.add(countryMobileCodeIndex);
                                indexList.put(countryMobileCodeIndex.getName(),countryMobileCodeList.size() - 1);
                            }

                            if (isMatchKeyword) {
                                priorityList.add(countryMobileCode);
                            }else {
                                countryMobileCodeList.add(countryMobileCode);
                            }
                        }
                        break;
                }
                event = xmlPullParser.next();
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(countryMobileCodeList);
        Collections.sort(countryIndexCodeList);
        countryMobileCodeList.addAll(0,priorityList);
    }

    public int getIndexPosition(String index) {
        if (!TextUtils.isEmpty(keyword)) {
            return getIndexPositionSearch(index);
        }
        try {
            Integer value = indexList.get(index);
            return value == null ? -1 : value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getIndexPositionSearch(String index) {
        try {
            Integer value = indexListSearch.get(index);
            return value == null ? -1 : value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class LoadMobileCodeTaskRunnable implements Runnable {
        private LoadMobileCodeTaskRunnable(){
        }
        @Override
        public void run() {
            loadMobileCodeList();
            notifyUpdateCountryMobileCodeList(countryMobileCodeList);
            isLoading = false;
        }
    }

    private class SearchMobileCodeTaskRunnable implements Runnable {

        @Override
        public void run() {
            search();
            notifyUpdateCountryMobileCodeList(countryMobileCodeListSearch);
        }

        private void search(){
            if (TextUtils.isEmpty(keyword)) {
                countryMobileCodeListSearch = countryMobileCodeList;
                return;
            }
            countryMobileCodeListSearch.clear();
            indexListSearch.clear();
            String tmpNameCode = "";
            for (CountryMobileCode countryMobileCode : countryMobileCodeList){
                if (countryMobileCode.isTitleType()) { // title type 不参与查询
                    continue;
                }
                if (isMatchKeyword(countryMobileCode,keyword)) {
                    String index = countryMobileCode.getName_code().substring(0,1);

                    if (!TextUtils.equals(tmpNameCode,index)) {
                        CountryMobileCode countryMobileCodeIndex = new CountryMobileCode();
                        countryMobileCodeIndex.setTitleType();
                        countryMobileCodeIndex.setName(index);
                        countryMobileCodeIndex.setName_code(index);
                        countryMobileCodeListSearch.add(countryMobileCodeIndex);
                        indexListSearch.put(index,countryMobileCodeListSearch.size() - 1);
                        tmpNameCode = index;
                    }
                    countryMobileCodeListSearch.add(countryMobileCode);
                }
            }
        }
    }


    private boolean isMatchKeyword(CountryMobileCode countryMobileCode,String keyword){
       return countryMobileCode.getName().contains(keyword) || countryMobileCode.getName_code().contains(keyword.toUpperCase());
    }

    private void notifyUpdateCountryMobileCodeList(List<CountryMobileCode> countryMobileCodeList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (TaskCompleteListener taskCompleteListener : taskCompleteListenerList){
                    taskCompleteListener.updateCountryMobileCodeList(countryMobileCodeList, countryIndexCodeList);
                }
            }
        });
    }

    public void destroy(){
        keyword = "";
        if (countryMobileCodeList != null) {
            countryMobileCodeList.clear();
        }
        if (countryIndexCodeList != null) {
            countryIndexCodeList.clear();
        }
        if (indexList != null) {
            indexList.clear();
        }
    }

    public interface TaskCompleteListener{
        void updateCountryMobileCodeList(List<CountryMobileCode> countryMobileCodeList, List<String> countryMobileIndexList);
    }

}
