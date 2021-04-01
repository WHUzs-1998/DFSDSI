package com.citydata.datacheck.operator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citydata.datacheck.entity.JsonData;
import com.citydata.datacheck.entity.Location;

import java.io.*;
import java.text.ParseException;

public class JsonFileRead {
    public static JsonData readJsonFile(File file) throws IOException, ParseException {
        FileReader fileReader = new FileReader(file);
        Reader reader = new InputStreamReader(new FileInputStream(file),"Utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        String jsonStr = sb.toString();
        JSONObject jobj = JSON.parseObject(jsonStr);
        //存储JSON文件的对象
        JsonData jsondata = new JsonData();
        //采集设备信息********************
        JSONObject deviceInfo = jobj.getJSONObject("deviceInfo");
        String typemodel = (String) deviceInfo.get("typemodel");
        String manufacturer= (String) deviceInfo.get("manufacturer");
        jsondata.setTypemodel(typemodel);
        jsondata.setManufacturer(manufacturer);

        //数据组织信息*********************
        JSONObject dataOrganizationInfo = jobj.getJSONObject("dataOrganizationInfo");
        String filetype = (String) dataOrganizationInfo.get("filetype");
        String dataFileNum= (String) dataOrganizationInfo.get("dataFileNum");
        String dataFileName = (String) dataOrganizationInfo.get("dataFileName");
        String relatedSoftware= (String) dataOrganizationInfo.get("relatedSoftware");
        jsondata.setDataFileName(dataFileName);
        jsondata.setRelatedSoftware(relatedSoftware);
        jsondata.setFiletype(filetype);
        jsondata.setDataFileNum(dataFileNum);
        //时空信息************************
        JSONObject spatiotemporalInfo = jobj.getJSONObject("spatiotemporalInfo");
        //**空间信息**********************
        JSONObject spatialInfo = spatiotemporalInfo.getJSONObject("spatialInfo");

        JSONArray centerLocation = spatialInfo.getJSONArray("centerLocation");
        for(int i=0;i<centerLocation.size();i++){
            Location location = new Location();
            location.setLon((String)centerLocation.getJSONObject(i).get("longitude"));
            location.setLat((String)centerLocation.getJSONObject(i).get("latitude"));
            location.setAlt((String)centerLocation.getJSONObject(i).get("altitude"));
            jsondata.centerLocation.add(location);
        }
        JSONArray regionCorners = spatialInfo.getJSONArray("regionCorners");
        for(int i=0;i<regionCorners.size();i++){
            Location location = new Location();
            location.setLat((String)regionCorners.getJSONObject(i).get("longitude"));
            location.setLon((String)regionCorners.getJSONObject(i).get("latitude"));
            jsondata.regionCorners.add(location);
        }
        String mapProjection = (String) spatialInfo.get("mapProjection");
        String zoningMethod = (String) spatialInfo.get("zoningMethod");
        String projectionZone = (String) spatialInfo.get("projectionZone");
        String pCoordinate = (String) spatialInfo.get("pCoordinate");
        String elevationSource = (String) spatialInfo.get("elevationSource");
        jsondata.setMapProjection(mapProjection);
        jsondata.setZoningMethod(zoningMethod);
        jsondata.setProjectionZone(projectionZone);
        jsondata.setPCoordinate(pCoordinate);
        jsondata.setElevationSource(elevationSource);
        //**时间信息**********************
        JSONObject timeInfo = spatiotemporalInfo.getJSONObject("timeInfo");
        JSONArray getTime = timeInfo.getJSONArray("getTime");
        for(int i=0;i<getTime.size();i++){
            jsondata.getTime.add((String)getTime.get(i));
        }
        System.out.println(jsondata.getTime.get(0));
        String makeTime = (String) timeInfo.get("makeTime");
        jsondata.setMakeTime(makeTime);
        //数据组织信息*********************
        JSONObject dataContentInfo = jobj.getJSONObject("dataContentInfo");
        String dataType = (String) dataContentInfo.get("dataType");
        String perceiveObject = (String) dataContentInfo.get("perceiveObject");
        jsondata.setDataType(dataType);
        jsondata.setPerceiveObject(perceiveObject);
        return jsondata;
    }
}
