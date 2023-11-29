package employeemanagementsystem;


import java.sql.Date;

public class jobData {
    
    private Integer jobId;
    private String location;
    private String description;
    private String gender;
    private String phoneNum;
    private String position;
    private String image;
    private Date date;
    private Double salary;
    
    public jobData(Integer jobId, String location, String description, String gender, String phoneNum, String position, String image, Date date){
        this.jobId = jobId;
        this.location = location;
        this.description = description;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.position = position;
        this.image = image;
        this.date = date;
    }
    public jobData(Integer jobId, String location, String description,String position, Double salary){
        this.jobId = jobId;
        this.location = location;
        this.description = description;
        this.position = position;
        this.salary = salary;
    }
    
    public Integer getJobId(){
        return jobId;
    }
    public String getLocation(){
        return location;
    }
    public String getDescription(){
        return description;
    }
    public String getGender(){
        return gender;
    }
    public String getPhoneNum(){
        return phoneNum;
    }    
    public String getPosition(){
        return position;
    }
    public String getImage(){
        return image;
    }
    public Date getDate(){
        return date;
    }
    public Double getSalary(){
        return salary;
    }
}
