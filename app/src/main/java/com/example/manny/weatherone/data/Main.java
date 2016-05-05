
package com.example.manny.weatherone.data;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Main {

    private Double temp;
    private Double pressure;
    private Integer humidity;
    private Double temp_min;
    private Double temp_max;
    private Double sea_level;
    private Double grnd_level;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Main() {
    }

    /**
     * 
     * @param humidity
     * @param pressure
     * @param temp_max
     * @param sea_level
     * @param temp_min
     * @param temp
     * @param grnd_level
     */
    public Main(Double temp, Double pressure, Integer humidity, Double temp_min, Double temp_max, Double sea_level, Double grnd_level) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.sea_level = sea_level;
        this.grnd_level = grnd_level;
    }

    /**
     * 
     * @return
     *     The temp
     */
    public Double getTemp() {
        return temp;
    }

    /**
     * 
     * @param temp
     *     The temp
     */
    public void setTemp(Double temp) {
        this.temp = temp;
    }

    /**
     * 
     * @return
     *     The pressure
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * 
     * @param pressure
     *     The pressure
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * 
     * @return
     *     The humidity
     */
    public Integer getHumidity() {
        return humidity;
    }

    /**
     * 
     * @param humidity
     *     The humidity
     */
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    /**
     * 
     * @return
     *     The temp_min
     */
    public Double getTemp_min() {
        return temp_min;
    }

    /**
     * 
     * @param temp_min
     *     The temp_min
     */
    public void setTemp_min(Double temp_min) {
        this.temp_min = temp_min;
    }

    /**
     * 
     * @return
     *     The temp_max
     */
    public Double getTemp_max() {
        return temp_max;
    }

    /**
     * 
     * @param temp_max
     *     The temp_max
     */
    public void setTemp_max(Double temp_max) {
        this.temp_max = temp_max;
    }

    /**
     * 
     * @return
     *     The sea_level
     */
    public Double getSea_level() {
        return sea_level;
    }

    /**
     * 
     * @param sea_level
     *     The sea_level
     */
    public void setSea_level(Double sea_level) {
        this.sea_level = sea_level;
    }

    /**
     * 
     * @return
     *     The grnd_level
     */
    public Double getGrnd_level() {
        return grnd_level;
    }

    /**
     * 
     * @param grnd_level
     *     The grnd_level
     */
    public void setGrnd_level(Double grnd_level) {
        this.grnd_level = grnd_level;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
