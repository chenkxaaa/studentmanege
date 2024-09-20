x
<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin-right: 10px" v-model="data.username" placeholder="请输入账号查询"
                :prefix-icon="Search"/>
      <el-input style="width: 260px; margin-right: 10px" v-model="data.name" placeholder="请输入学生名称查询"
                :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left:10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="序号" width="100"/>
          <el-table-column prop="username" label="学生账号"/>
          <el-table-column prop="name" label="学生的名称"/>
          <el-table-column prop="phone" label="学生手机号"/>
          <el-table-column prop="email" label="学生邮箱"/>
          <el-table-column prop="sex" label="性别"/>
          <el-table-column prop="birth" label="生日"/>
          <el-table-column prop="avatar" label="头像">
            <template #default="scope">
              <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 40px; height: 40px; border-radius: 5px"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180px">
            <template #default="scope">
              <el-button type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
              <el-button type="info" plain @click="handleDelete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     @current-change="handelCurrentChange"
                     background layout="prev, pager, next"
                     :total="data.total"/>
    </div>

    <el-dialog v-model="data.formVisible" title="课程信息" width="35%" destroy-on-close>
      <el-form :model="data.form" :rules="rules" ref="formRef" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="学生账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="学生密码" prop="password">
          <el-input show-password v-model="data.form.password" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="学生姓名" >
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="data.form.phone" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="data.form.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker format="YYYY-MM-DD" value-format="YYYY-MM-DD" v-model="data.form.birth"></el-date-picker>
        </el-form-item>
        <el-form-item label="学生头像">
          <el-upload action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess" >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
           <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {ref,reactive} from "vue";
import {Search} from '@element-plus/icons-vue'
import request from "../../utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

//统一路径
const baseUrl = '/student'

const data = reactive({
  username: '',
  name: '',
  tableData: [],
  total: 0,
  pageSize: 5,  //每页的个数
  pageNum: 1,   //当前的页码
  formVisible: false,
  form: {}

})

//从后台获取信息
const load = () => {
  request.get(baseUrl+'/selectPage', {
    params: {
      pageNum: data.pageNum,    //绑定页码
      pageSize: data.pageSize,   //绑定表示每业的个数
      name: data.name,    //绑定前端的查询接口
      username: data.username
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })

}

//重置按钮的实现
const reset = () => {
  data.username = ''
  data.name = ''
  load()
}

//表单校验规则
const rules =reactive({
  username:[
    {required: true,message:"请输入账号",trigger: 'blur'}
  ],
  password:[
    {required: true,message:"请输入密码",trigger: 'blur'}
  ],
})

const formRef = ref()

//新增课程信息
const handleAdd = () => {
  data.form = {}   //清空
  data.formVisible = true   //打开弹窗
}

//调用方法获取后台数据
load()


// 保存数据到后台
const save = () => {
  formRef.value.validate((valid)=> {
    //console.log(valid)
    if (valid) {
      request.request({
        url: data.form.id ? baseUrl+ '/update' : baseUrl+ '/add',
        method: data.form.id ? 'PUT' : 'POST',          //??????????????????????????
        data: data.form
      }).then(res => {
        if (res.code === '200') {
          load()    // 重新获取数据
          data.formVisible = false  // 关闭弹窗
          ElMessage.success("操作成功")
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })//请求

}

//编辑课程信息（修改课程信息）
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))  //拷贝原先数据表上的数据
  data.formVisible = true
}

const handelCurrentChange = (pageNum) => {   //分页的组件
  //当翻页的术后重新加载数据即可
  load()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete(baseUrl+ '/delete/' + id).then(res => {
      if (res.code === '200') {
        load()    // 重新获取数据
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
  })
}

//上传学生图片
const handleImgUploadSuccess = (res) => {
  data.form.avatar = res.data
}

</script>