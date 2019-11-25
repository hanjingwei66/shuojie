package com.shuojie.serverImpl.sensorServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.shuojie.domain.sensorModle.ZullProperty;
import com.shuojie.nettyService.Handler.TextWebSocketFrameHandler;
import com.shuojie.service.sensorService.Observer;
import com.shuojie.service.sensorService.SensorProperty;
import com.shuojie.service.sensorService.SensorSubject;
import com.shuojie.utils.autowiredUtil.SpringUtil;
import com.shuojie.utils.vo.Result;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 观察者类 激光传感器
 */
public class LaserSensorImpl implements Observer {
    @Value("${sensor.laserId}")
    private Integer laserId;
    private int jizhongqid;
    private int jiedianid;
    private int time;
    private byte[] LaserSensorData;
    private SensorSubject sensorData;
    public LaserSensorImpl(SensorSubject sensorData){
        this.sensorData=sensorData;
        sensorData.registerObserver(this);
    }

    ZullProperty zullProperty=new ZullProperty();
    ZullProperty zullPropertyVo=new ZullProperty();
    Result result=null;
    @Override
    public void update(int jizhongqid, int jiedianid, int time, byte[] dataBytes,int snr) {
        if (jiedianid == laserId) {
            TextWebSocketFrameHandler textWebSocketFrameHandler = (TextWebSocketFrameHandler) SpringUtil.getBean("TextWebSocketFrameHandler");
            SensorProperty sensorProperty = (SensorProperty) SpringUtil.getBean("SensorProperty");
            this.jizhongqid = jizhongqid;
            this.jiedianid = jiedianid;
            this.time = time;
            this.LaserSensorData = dataBytes;
            if (dataBytes != null) {
                //电压
                Integer VD00 = sensorProperty.computeVoltage(dataBytes[0], dataBytes[1], dataBytes[2]);
                Integer VD01 = sensorProperty.computeVoltage(dataBytes[3], dataBytes[4], dataBytes[5]);
                Integer VD10 = sensorProperty.computeVoltage(dataBytes[6], dataBytes[7], dataBytes[8]);
                Integer VD11 = sensorProperty.computeVoltage(dataBytes[9], dataBytes[10], dataBytes[11]);
//                           //激光点的xy轴坐标
                Double Px = sensorProperty.computeSupersonic(dataBytes[12], dataBytes[13], dataBytes[14]);
                Double Py = sensorProperty.computeSupersonic(dataBytes[15], dataBytes[16], dataBytes[17]);
//                          //超声波测距
                Double Distance = sensorProperty.computeDistance(dataBytes[18], dataBytes[19]);

//                        Integer DataL = sensorProperty.computeTENAXIS(dataBytes[20], dataBytes[21]);
                //加速度xyz
                double[] AccelerationList = sensorProperty.computeAcceleration(dataBytes[20], dataBytes[21], dataBytes[22], dataBytes[23], dataBytes[24], dataBytes[25]);
////                       //角速度xyz
                double[] AngularVelocityList = sensorProperty.computeAngularVelocity(dataBytes[26], dataBytes[27], dataBytes[28], dataBytes[29], dataBytes[30], dataBytes[31]);
//                        //角度
                double[] AngularList = sensorProperty.computeAngular(dataBytes[32], dataBytes[33], dataBytes[34], dataBytes[35], dataBytes[36], dataBytes[37]);
//                        //高度
                double hight = sensorProperty.computeHight(dataBytes[38], dataBytes[39], dataBytes[40], dataBytes[41]);
////                        tt.setSensorData(dataBytes.toString());
                zullProperty.setJiedianid(jiedianid);
                zullProperty.setTime(time);
                zullProperty.setVoltage1(VD00);
                zullProperty.setVoltage2(VD01);
                zullProperty.setVoltage3(VD10);
                zullProperty.setVoltage4(VD11);
                zullProperty.setXSupersonic(Px);
                zullProperty.setYSupersonic(Py);
                zullProperty.setDistance(Distance);
                zullProperty.setAcceleration(AccelerationList);
                zullProperty.setAngularVelocity(AngularVelocityList);
                zullProperty.setAngular(AngularList);
                zullProperty.setHight(hight);
                zullProperty.setXAcceleration(AccelerationList[0]);
                zullProperty.setYAcceleration(AccelerationList[1]);
                zullProperty.setZAcceleration(AccelerationList[2]);
                zullProperty.setXAngularVelocity(AngularVelocityList[0]);
                zullProperty.setYAcceleration(AngularVelocityList[1]);
                zullProperty.setZAcceleration(AngularVelocityList[2]);
                zullProperty.setXAngular(AngularList[0]);
                zullProperty.setYAngular(AngularList[1]);
                zullProperty.setZAngular(AngularList[2]);

                zullPropertyVo.setJiedianid(jiedianid);
                zullPropertyVo.setTime(time);
                zullPropertyVo.setVoltage1(VD00);
                zullPropertyVo.setVoltage2(VD01);
                zullPropertyVo.setVoltage3(VD10);
                zullPropertyVo.setVoltage4(VD11);
                zullPropertyVo.setXSupersonic(Px);
                zullPropertyVo.setYSupersonic(Py);
                zullPropertyVo.setDistance(Distance);
                zullPropertyVo.setAcceleration(AccelerationList);
                zullPropertyVo.setAngularVelocity(AngularVelocityList);
                zullPropertyVo.setAngular(AngularList);
                zullPropertyVo.setHight(hight);
                result = new Result(200, "success", "sensor_init", zullPropertyVo);
                String jsonzullProperty = JSONObject.toJSONString(result);
                textWebSocketFrameHandler.send(jsonzullProperty);
            }
        }
    }
    @Override
    public void remove(){
        sensorData.removeObserver(this);
    }
}
