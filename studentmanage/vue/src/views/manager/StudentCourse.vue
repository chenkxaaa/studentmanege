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
      <div>
        <el-table :data="data.tableData" style="width: 100%">
          <el-table-column prop="id" label="序号" width="100"/>
          <el-table-column prop="name" label="课程名称"/>
          <el-table-column prop="no" label="课程编号"/>
          <el-table-column prop="studentName" label="学生名称"/>
          <el-table-column label="操作" width="180px" v-if="user.role === 'ADMIN'">
            <template #default="scope">
              <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
              <el-button type="primary" @click="addGrade(scope.row)" v-if="user.role === 'ADMIN'">打分</el-button>
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

    <el-dialog v-model="data.formVisible" title="成绩信息" width="35%">
      <el-form :model="data.gradeForm" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.gradeForm.name" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="分数" prop="name">
          <el-input v-model="data.gradeForm.score" autocomplete="off" />
        </el-form-item>
        <el-form-item label="评语" prop="name">
          <el-input v-model="data.gradeForm.comment" autocomplete="off" />
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
  tableData: [],
  total: 0,
  pageSize: 5,  //每页的个数
  pageNum: 1,   //当前的页码
  user: JSON.parse(localStorage.getItem('student-user') || '{ }'),
  gradeForm:{},
  formVisible:false,//弹窗控制显示
})

//从后台获取信息
const load = () => {
  let params = {
    pageNum: data.pageNum,    //绑定页码
    pageSize: data.pageSize,   //绑定表示每业的个数
    name: data.name,
    no: data.no,
  }
  if (data.user.role === 'STUDENT') {  //如果当前登录是学生就查询自己的选课记录
    params.studentId = data.user.id
  }
  request.get('/studentCourse/selectPage', {
    params: params
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })

}
//重置按钮的实现
const reset = () => {
  data.name = ''
  data.no = ''
  load()
}

const handelCurrentChange = (pageNum) => {   //分页的组件
  //当翻页的术后重新加载数据即可
  load()
}

load();

//删除
const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/studentCourse/delete/' + id).then(res => {
      if (res.code === '200') {
        load()    // 重新获取数据
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {})
}

const addGrade = (row) =>{
  data.formVisible= true
  data.gradeForm.name=row.name
  data.gradeForm.courseId=row.courseId
  data.gradeForm.studentId=row.studentId
}

const save = () => {
  request.post('/grade/add',data.gradeForm).then(res =>{
    if (res.code === '200') {
      load()    // 重新获取数据
      data.formVisible = false  // 关闭弹窗
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>