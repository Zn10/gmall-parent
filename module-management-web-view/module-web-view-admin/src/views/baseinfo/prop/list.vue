<template>
  <div class="app-container">

    <!--三级下拉列表-->
    <CategorySelector @listenOnSelect="getAttrInfoList" />

    <!--属性列表-->
    <div v-show="!showAttrInfoForm">
      <div style="margin-bottom:10px;">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="addAttrInfo()">添加平台属性</el-button>
      </div>

      <el-table
        v-loading="attrInfoListLoading"
        :data="attrInfoList"
        element-loading-text="数据正在加载......"
        border
        fit
        highlight-current-row>
        <el-table-column align="center" label="序号" width="100">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="属性id" width="100">
          <template slot-scope="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column label="属性名称">
          <template slot-scope="scope">
            <span>{{ scope.row.attrName }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button v-if="categoryLevel === scope.row.categoryLevel" type="primary" size="mini" icon="el-icon-edit" @click="editAttrInfoById(scope.row.id, scope.row.attrName)">修改</el-button>
            <el-button v-if="categoryLevel === scope.row.categoryLevel" type="danger" size="mini" icon="el-icon-edit" @click="deleteAttrInfoById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--属性表单-->
    <el-form v-show="showAttrInfoForm" :model="attrInfoForm" :inline="true" class="demo-form-inline">

      <el-form-item label="属性名称">
        <el-input v-model="attrInfoForm.attrName"/>
      </el-form-item>

      <div style="margin-bottom:10px;">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="addAttrValue()">添加属性值</el-button>

      </div>
      <div>
        <el-table
          v-loading="attrValueListLoading"
          :data="attrInfoForm.attrValueList"
          element-loading-text="数据正在加载......"
          border
          fit
          highlight-current-row>
          <el-table-column align="center" label="序号" width="100">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="属性值id" width="100">
            <template slot-scope="scope">
              {{ scope.row.id }}
            </template>
          </el-table-column>
          <el-table-column label="属性值名称">
            <!-- <template slot-scope="scope">
              <span>{{ scope.row.valueName }}</span>
            </template> -->
            <template slot-scope="scope">
              <el-input
                v-if="scope.row.edit"
                v-model="scope.row.valueName"
                class="edit-input"
                size="mini"
                @keyup.enter.native="saveAttrValue(scope.row)"
                @blur="saveAttrValue(scope.row)" />
              <span
                v-else
                @click="editAttrValue(scope.row)">{{ scope.row.valueName }}</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" icon="el-icon-edit" @click="deleteAttrValueByName(scope.row.valueName)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!--查询按钮-->
      <div style="margin-top:22px;">
        <el-button type="primary" size="mini" @click="saveAttrInfo()">保存</el-button>
        <el-button type="default" size="mini" @click="backToAttrList()">返回</el-button>
      </div>
    </el-form>

  </div>
</template>

<script>
import prop from '@/api/baseinfo/prop'
import CategorySelector from '@/views/components/CategorySelector'

export default {
  components: { CategorySelector },
  data() {
    return {

      // 属性所属分类
      category1Id: 0,
      category2Id: 0,
      category3Id: 0,
      categoryId: 0,
      categoryLevel: 1,

      // 属性列表数据
      attrInfoList: null,
      attrInfoListLoading: false,

      // 属性表单数据
      showAttrInfoForm: false,
      attrValueListLoading: false,
      attrInfoForm: {
        id: null,
        attrName: null,
        category1Id: 0,
        category2Id: 0,
        category3Id: 0,
        attrValueList: []
      }
    }
  },

  methods: {

    // 获取属性列表
    getAttrInfoList(categoryId, categoryLevel) {
      this.categoryId = categoryId
      this.categoryLevel = categoryLevel
      // debugger
      if (categoryLevel === 1) {
        this.category1Id = categoryId
        this.category2Id = 0
        this.category3Id = 0
      }
      if (categoryLevel === 2) {
        this.category2Id = categoryId
        this.category3Id = 0
      }
      if (categoryLevel === 3) {
        this.category3Id = categoryId
      }
      // 查询数据
      this.attrInfoListLoading = true
      prop.getAttrInfoList(this.category1Id, this.category2Id, this.category3Id).then(response => {
        this.attrInfoList = response.data
        this.attrInfoListLoading = false
      })
    },

    // 添加平台属性
    addAttrInfo() {
      if (!this.confirmSelect()) {
        return
      }

      // 初始化值
      this.attrInfoForm.id = null
      this.attrInfoForm.attrName = null
      this.attrInfoForm.attrValueList = []

      // 显示表单
      this.showAttrInfoForm = true
    },

    // 选择三级分类确认
    confirmSelect() {
      if (!this.category1Id) {
        this.$alert('请选择分类', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        })
        return false
      }
      return true
    },

    // 修改属性
    editAttrInfoById(attrId, attrName) {
      // 获取属性值列表
      this.attrValueListLoading = true
      prop.getAttrValueList(attrId).then(response => {
        this.attrInfoForm.id = attrId
        this.attrInfoForm.attrName = attrName
        this.attrInfoForm.attrValueList = response.data
        this.attrValueListLoading = false
      })
      // 显示表单
      this.showAttrInfoForm = true
    },
    // 删除属性
    deleteAttrInfoById(attrId){
      // debugger
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return prop.remove(attrId)
      }).then((response) => {
        this.getAttrInfoList(this.categoryId, this.categoryLevel)
        if (response.code) {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    // 删除属性值
    deleteAttrValueByName(attrValueName) {
      const tempList = []
      this.attrInfoForm.attrValueList.forEach(attrValue => {
        if (attrValue.valueName !== attrValueName) {
          tempList.push(attrValue)
        }
      })
      this.attrInfoForm.attrValueList = tempList
    },

    // 保存属性和属性值
    saveAttrInfo() {
      this.attrInfoForm.categoryId = this.categoryId
      this.attrInfoForm.categoryLevel = this.categoryLevel
      prop.saveAttrInfo(this.attrInfoForm).then(response => {
        // 刷新属性列表
        this.getAttrInfoList(this.categoryId, this.categoryLevel)
        // 隐藏表单
        this.showAttrInfoForm = false
      })
    },

    // 返回属性列表页面
    backToAttrList() {
      // 隐藏表单
      this.showAttrInfoForm = false
    },

    // 添加属性值
    addAttrValue() {
      const attrValue = {
        valueName: null,
        edit: true
      }
      this.attrInfoForm.attrValueList.push(attrValue)
    },

    // 保存属性值
    saveAttrValue(row) {
      row.edit = false
    },

    // 编辑属性值
    editAttrValue(row) {
      row.edit = true
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 60px;
}
.save-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>

