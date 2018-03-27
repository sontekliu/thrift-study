namespace java com.javaliu.thrift.service

typedef i16 short
typedef i32 int
typedef i64 long
typedef bool boolean
typedef string String

struct Student{
    1 : optional long id;
    2 : optional String name;
    3 : optional boolean sex;
}

exception ServiceException{
    1 : optional int code;
    2 : optional String message;
    3 : optional String date;
}

service StudentService{
    /*
        根据学生ID 查询学生信息
    */
    Student findStudentById(1:required int id) throws (1:ServiceException serviceException)

    /*
        保存学生信息
     */
    void saveStudent(1:required Student student) throws (1:ServiceException serviceException)
}