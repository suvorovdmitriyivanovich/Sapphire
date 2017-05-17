package com.sapphire.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PayrollInformationData {
    private String payrollInformationId = "";
    private String employeeNumber = "";
    private String punchClockNumber = "";
    private Double hoursPerDay = 0d;
    private Double salary = 0d;
    private Double payRate = 0d;
    private String payFrequencyId = "";
    private String payFrequency = "";
    private String payTypeId = "";
    private String payType = "";
    private String payrollGroupId = "";
    private String payrollGroup = "";
    private String profileId = "";

    public PayrollInformationData() {

    }

    public PayrollInformationData(JSONObject data) {
        try {
            if (!data.isNull("PayrollInformationId")) {
                setPayrollInformationId(data.getString("PayrollInformationId"));
            }
            if (!data.isNull("EmployeeNumber")) {
                setEmployeeNumber(data.getString("EmployeeNumber"));
            }
            if (!data.isNull("PunchClockNumber")) {
                setPunchClockNumber(data.getString("PunchClockNumber"));
            }
            if (!data.isNull("HoursPerDay")) {
                setHoursPerDay(data.getDouble("HoursPerDay"));
            }
            if (!data.isNull("Salary")) {
                setSalary(data.getDouble("Salary"));
            }
            if (!data.isNull("PayRate")) {
                setPayRate(data.getDouble("PayRate"));
            }
            if (!data.isNull("PayFrequencyId")) {
                setPayFrequencyId(data.getString("PayFrequencyId"));
            }
            if (!data.isNull("PayFrequency")) {
                setPayFrequency(data.getString("PayFrequency"));
            }
            if (!data.isNull("PayTypeId")) {
                setPayTypeId(data.getString("PayTypeId"));
            }
            if (!data.isNull("PayType")) {
                setPayType(data.getString("PayType"));
            }
            if (!data.isNull("PayrollGroupId")) {
                setPayrollGroupId(data.getString("PayrollGroupId"));
            }
            if (!data.isNull("PayrollGroup")) {
                setPayrollGroup(data.getString("PayrollGroup"));
            }
            if (!data.isNull("ProfileId")) {
                setProfileId(data.getString("ProfileId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPayrollInformationId(String payrollInformationId) {
        this.payrollInformationId = payrollInformationId;
    }

    public String getPayrollInformationId() {
        return payrollInformationId;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setPunchClockNumber(String punchClockNumber) {
        this.punchClockNumber = punchClockNumber;
    }

    public String getPunchClockNumber() {
        return punchClockNumber;
    }

    public Double getHoursPerDay() {
        return hoursPerDay;
    }

    public String getHoursPerDayString() {
        if (hoursPerDay == 0d) {
            return "";
        } else {
            return String.valueOf(hoursPerDay);
        }
    }

    public void setHoursPerDay(Double hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public Double getSalary() {
        return salary;
    }

    public String getSalaryString() {
        if (salary == 0d) {
            return "";
        } else {
            return String.valueOf(salary);
        }
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getPayRate() {
        return payRate;
    }

    public void setPayRate(Double payRate) {
        this.payRate = payRate;
    }

    public String getPayFrequencyId() {
        return payFrequencyId;
    }

    public void setPayFrequencyId(String payFrequencyId) {
        this.payFrequencyId = payFrequencyId;
    }

    public String getPayFrequency() {
        return payFrequency;
    }

    public void setPayFrequency(String payFrequency) {
        this.payFrequency = payFrequency;
    }

    public String getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(String payTypeId) {
        this.payTypeId = payTypeId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayrollGroupId() {
        return payrollGroupId;
    }

    public void setPayrollGroupId(String payrollGroupId) {
        this.payrollGroupId = payrollGroupId;
    }

    public String getPayrollGroup() {
        return payrollGroup;
    }

    public void setPayrollGroup(String payrollGroup) {
        this.payrollGroup = payrollGroup;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}