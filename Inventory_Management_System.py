from flask import *
import mysql.connector
app=Flask(__name__)

server= mysql.connector.connect(host = "localhost", user = "root",password = "326459",port=3306)
handler=server.cursor()
def make_DB():
    handler.execute("create database if not exists Inventory")
    handler.execute("use Inventory")


@app.route("/add_stock",methods=["POST"])
def add_stock():
    product_name=request.json["product"]
    product_amount=request.json["amount"]
    if(product_amount<=0):
        return jsonify({"message":"Enter a valid amount"})
    product_expiration=request.json["expiration"]
    product_mark=request.json["mark"]

    handler.execute("use Inventory")
    handler.execute(
        "create table if not exists inventory(id int auto_increment primary key,product varchar(15)not null,amount int not null,mark int not null,expiration datetime not null)"
        )
    
    handler.execute("insert into inventory(product,amount,mark,expiration)values(%s,%s,%s,%s)",(product_name,product_amount,product_mark,product_expiration))
    server.commit()
    return jsonify({"message":"stock added"})
def renumber(lst):
    return [tuple([i+1]) + tuple(x[1:]) for i, x in enumerate(lst)]
@app.route("/remove_stock",methods=["DELETE","PUT"])
def remove_stock():
    product_name = request.json["product"]
    product_amount = int(request.json["amount"])
    if request.method == "PUT" or request.method=="DELETE":
        handler.execute("use inventory")
        handler.execute("select * from inventory where product=%s", (product_name,))
        data = handler.fetchall()
        data.sort(key=lambda x: x[0])
        new_records = [list(row) for row in data]
        remaining_amount = product_amount
        for row in new_records:
            if remaining_amount <= 0:
                break
            if row[2] >= remaining_amount:
                row[2] -= remaining_amount
                remaining_amount = 0
            else:
                remaining_amount -= row[2]
                row[2] = 0
        new_records = [row for row in new_records if row[2] > 0]
        new_records=renumber(new_records)
        handler.execute("Delete from inventory where product=%s",(product_name,))
        print(new_records)
        for row in new_records:
            handler.execute("Insert into inventory(product,amount,mark,expiration)values(%s,%s,%s,%s)",row[1:])
        server.commit()
        return jsonify({"message": "everything"})

@app.route("/view_stock",methods=["GET"])
def view_stock():
    product_name=request.json["product"]
    handler.execute("use inventory")
    if(str(product_name).lower()=="all"):
        handler.execute("select * from inventory")
        data=handler.fetchall()
    else:
        handler.execute("select * from inventory where product=%s",(product_name,))
        data=handler.fetchall()
        if(len(data)==0):
            return jsonify({"message":"none in stock"})
    send_data=data
    while(len(send_data)!=0):
        jsonify({"message":send_data[0]})
        send_data=send_data[1:]

@app.route("/add_user",methods=["POST"])
def add_user():
    user_name=request.json["username"]
    user_password=request.json["password"]
    user_role=request.json["role"]
    if(user_name==""or user_password==""):
        return jsonify({"message":"Try again"})
    else:
        handler.execute("use inventory")
        handler.execute(
        "create table if not exists users(id int auto_increment primary key,username varchar(15)not null,password varchar(15) not null,role varchar(15))"
        )
        handler.execute("select * from users where username=%s",(user_name,))
        data=handler.fetchall()
        if(len(data)!=0):
            return jsonify({"message":"Username taken"})
        else:
            handler.execute("insert into users(username,password,role)values(%s,%s,%s)",(user_name,user_password,user_role))
            server.commit()
            return jsonify({"message":"Account Created Successfully"})
@app.route("/remove_user",methods=["DELETE"])
def remove_user():
    user_name=request.json["username"]
    if(user_name==""):
        return jsonify({"message":"Try again"})
    else:
        handler.execute("use inventory")
        handler.execute("delete from users where username=%s",(user_name,))
        server.commit()
        return jsonify({"message":"Account Deleted Successfully"})


if __name__ == "__main__":
    make_DB()
    app.run(debug=True) 