package com.shuojie.serverImpl.sensorServiceImpl;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuojie.dao.sensorMappers.LaserSensorMapper;
import com.shuojie.dao.sensorMappers.SensorMapper;
import com.shuojie.domain.sensorModle.LaserSensor;
import com.shuojie.domain.sensorModle.ZullProperty;
import com.shuojie.service.sensorService.LaserSensorService;
import com.shuojie.utils.timeUtil.TimeFormateUtil;
import com.shuojie.utils.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LaserSensorImpl1 implements LaserSensorService {
        @Autowired
        private LaserSensorMapper LaserMapper;
        private Result result;
        @Override
        public Result selectPage(Page<LaserSensor> page, String startTime,String endTime) {
            if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
                Long startStamp = TimeFormateUtil.timeToStamp(startTime);
                Long endStamp = TimeFormateUtil.timeToStamp(endTime);
                QueryWrapper<LaserSensor> queryWrapper = new QueryWrapper<>();
                queryWrapper.between("user_time", startTime, endTime);
                IPage<LaserSensor> iPage = LaserMapper.selectPage(page, queryWrapper);
                List<LaserSensor> laserSensor = iPage.getRecords();

                for (LaserSensor zu:laserSensor) {
                    Double xAcceleration = zu.getXAcceleration();
                    Double yAcceleration = zu.getYAcceleration();
                    Double zAcceleration = zu.getZAcceleration();
                    double[] acceleration = new double[3];
                    acceleration[0]=xAcceleration;
                    acceleration[1]=yAcceleration;
                    acceleration[2]=zAcceleration;
                    zu.setAcceleration(acceleration);
                    Double xAngularVelocity = zu.getXAngularVelocity();
                    Double yAngularVelocity = zu.getYAngularVelocity();
                    Double zAngularVelocity = zu.getZAngularVelocity();
                    double[] angularVelocity = new double[3];
                    angularVelocity[0]=xAngularVelocity;
                    angularVelocity[1]=yAngularVelocity;
                    angularVelocity[2]=zAngularVelocity;
                    zu.setAngularVelocity(angularVelocity);
                    Double xAngular = zu.getXAngular();
                    Double yAngular = zu.getYAngular();
                    Double zAngular = zu.getZAngular();
                    double[] angular = new double[3];
                    angular[0]=xAngular;
                    angular[1]=yAngular;
                    angular[2]=zAngular;
                    zu.setAngular(angular);
                    zu.setXAcceleration(null);
                    zu.setXAngular(null);
                    zu.setXAngularVelocity(null);
                    zu.setYAcceleration(null);
                    zu.setYAngular(null);
                    zu.setYAngularVelocity(null);
                    zu.setZAcceleration(null);
                    zu.setZAngular(null);
                    zu.setZAngularVelocity(null);
                }
                long pages = iPage.getPages();
                long total = iPage.getTotal();
                long current = iPage.getCurrent();
                long size = iPage.getSize();
                result = new Result(200, "SUCCESS", "sensor_findLog_byTime", laserSensor);
                result.setTotal(total);
                result.setPages(pages);
                result.setCurrentPage(current);
                result.setPageSize(size);
            }
            return result;
        }


    }