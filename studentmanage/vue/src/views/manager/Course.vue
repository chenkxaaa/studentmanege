<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin-right: 10px" v-model="data.name" placeholder="请输入课程名称"
                :prefix-icon="Search"/>
      <el-input style="width: 260px; margin-right: 10px" v-model="data.no" placeholder="请输入课程编号" :prefix-icon="Search"/>
      <el-input style="width: 260px" v-model="data.teacher" placeholder="请输入任课老师" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left:10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd"  v-if="user.role === 'ADMIN'">新增</el-button>
      </div>

      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="序号" width="100"/>
          <el-table-column prop="name" label="课程名称"/>
          <el-table-column prop="no" label="课程编号"/>
          <el-table-column prop="see" label="课程描述"/>
          <el-table-column prop="times" label="课时"/>
          <el-table-column prop="teacher" label="任课老师"/>
          <el-table-column label="操作" width="180px">
            <template #default="scope">
              <el-button type="primary" plain @click ="handleEdit(scope.row)"  v-if="user.role === 'ADMIN'">编辑</el-button>
              <el-button type="info" plain @click="handleDelete(scope.row.id)"  v-if="user.role === 'ADMIN'">删除</el-button>
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

    <el-dialog v-model="data.formVisible" title="课程信息" width="35%">
      <el-form :rules="rules" ref="formRef" :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程编号" prop="no">
          <el-input v-model="data.form.no" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="data.form.see" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课时">
          <el-input v-model="data.form.times" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="任课老师" prop="teacher">
          <el-input v-model="data.form.teacher" autocomplete="off"/>
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
import {reactive, ref,} from "vue";
import {Search} from '@element-plus/icons-vue'
import request from "../../utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";


const user = JSON.parse(localStorage.getItem('student-user') || '{}')
const data = reactive({
  name: '',
  no: '',
  teacher: '',
  see: '',
  times: '',
  tableData: [],
  total: 0,
  pageSize: 5,  //每页的个数
  pageNum: 1,   //当前的页码
  formVisible: false,
  form: {}

})

//从后台获取信息
const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,    //绑定页码
      pageSize: data.pageSize,   //绑定表示每业的个数
      name: data.name,
      no: data.no,
      teacher: data.teacher
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })

}
//重置按钮的实现
const reset = () => {
  data.name = ''
  data.no = ''
  data.teacher=''
  load()
}
//表单校验规则
const rules =reactive({
  name:[
    {required: true,message:"请输入课程名称",trigger: 'blur'}
  ],
  no:[
    {required: true,message:"请输入课程编号",trigger: 'blur'}
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
        url: data.form.id? '/course/update' : '/course/add',
        method: data.form.id?  'PUT': 'POST' ,          //??????????????????????????
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
  })
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

const  handleDelete= (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/course/delete/' + id).then(res => {
      if (res.code === '200') {
        load()    // 重新获取数据
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {})
}

</script>