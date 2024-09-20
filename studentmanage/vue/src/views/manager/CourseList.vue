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
          <el-table-column prop="see" label="课程描述"/>
          <el-table-column prop="times" label="课时"/>
          <el-table-column prop="teacher" label="任课老师"/>
          <el-table-column label="操作" width="180px">
            <template #default="scope">
              <el-button type="primary" @click="selectCourse(scope.row)" >选课</el-button>
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


  </div>
</template>

<script setup>
import {reactive, ref,} from "vue";
import {Search} from '@element-plus/icons-vue'
import request from "../../utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";



const data = reactive({
  name: '',
  no: '',
  teacher: '',
  tableData: [],
  total: 0,
  pageSize: 5,  //每页的个数
  pageNum: 1,   //当前的页码
  student: JSON.parse(localStorage.getItem('student-user') || '{ }')

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
  data.teacher = ''
  load()
}

const handelCurrentChange = (pageNum) => {   //分页的组件
  //当翻页的术后重新加载数据即可
  load()
}

load();


const selectCourse = (row) => {
  request.post('studentCourse/add', { studentId: data.student.id , name:row.name, no:row.no, courseId:row.id}).then( res =>{
    if (res.code === '200') {
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>