namespace java com.javaliu.thrift.service
namespace py com.javaliu.py

typedef i16 short
typedef i32 int
typedef i64 long
typedef bool boolean
typedef string String

struct User{
    1 : optional long id;
    2 : optional String code;
    3 : optional String name;
    4 : optional String email;
}

exception ServiceException{
    1 : optional int code;
    2 : optional String message;
    3 : optional String date;
}

service UserService{
    /*
        根据用户 ID 查询用户信息
    */
    User findUserById(1:required long id) throws (1:ServiceException serviceException)

    /*
        保存用户信息
     */
    void saveUser(1:required User user) throws (1:ServiceException serviceException)
}