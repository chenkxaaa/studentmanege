



<template>
    <div>
        <div class="login-container">
            <div style="width: 350px" class="login-box">
              <div class="tilte">注册</div>
                <el-form :model="data.form" ref="formRef" rules="rules">
                    <el-form-item prop="username">
                        <el-input prefix-icon = "UserFilled" v-model="data.form.username" placeholder = "请输入账号"/>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input  show-password prefix-icon = "Lock" v-model="data.form.password" placeholder = "请输入密码"/>
                    </el-form-item>

                    <el-form-item>
                      <el-button type="primary" class="login-button" @click="register">注册</el-button>
                    </el-form-item>

                </el-form>
              <div style="margin-top: 15px;text-align: right">
                  点<a href="/login">我</a>登录
              </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import {reactive,ref}  from "vue";  //引用内置对象
import request from "../utils/request";
import {ElMessage} from "element-plus";
import router from "../router";


const data = reactive({
    form :{}
})

const rules =reactive({
  name:[
    {required: true,message:"请输入密码",trigger: 'blur'}
  ],
  password:[
    {required: true,message:"请输入密码",trigger: 'blur'}
  ],
})

const formRef=ref()

const register=()=>{
  formRef.value.validate((valid)=> {
    //console.log(valid)
    if (valid) {//请求
      request.post("/register",data.form).then(res => {
        if (res.code === "200") {
          ElMessage.success("注册成功")
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>

<style scoped>
.login-container{
    min-height: 100vh;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: seashell;


}
/***登录盒子***/
.login-box{
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0,0,0,0.1);
    padding: 30px;
  border-radius: 10px;
}
/***标题***/
.tilte{
  text-align: center;
  margin-bottom: 30px;
}

.login-button{
  text-align: center;
  width: 100%;
}
</style>