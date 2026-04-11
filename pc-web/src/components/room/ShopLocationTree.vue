<template>
  <el-card class="tree-card">
    <div slot="header" class="clearfix">
      <span>{{ $t('shopLocation.title') || '商铺位置' }}</span>
      <el-button v-if="hasPrivilege('502020082453087940')" type="text" size="mini" style="float: right;" @click="handleAdd">{{ $t('common.add') }}</el-button>
    </div>
    <el-tree
      ref="tree"
      :data="treeData"
      node-key="locationId"
      :props="defaultProps"
      :highlight-current="true"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>{{ node.label }}</span>
        <span>
          <el-button v-if="hasPrivilege('502020082493857941')" type="text" size="mini" @click="handleEdit(data)">{{ $t('common.edit') }}</el-button>
          <el-button v-if="hasPrivilege('502020082417457942')" type="text" size="mini" @click="handleDelete(data)">{{ $t('common.delete') }}</el-button>
          <el-button v-if="hasPrivilege('502020082453087940')" type="text" size="mini" @click="handleAddChild(data)">+</el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="handleDialogClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('shopLocation.locationName') || '位置名称'" prop="locationName">
          <el-input v-model="form.locationName" />
        </el-form-item>
        <el-form-item :label="$t('shopLocation.remark') || '备注'" prop="remark">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
        <el-form-item :label="$t('shopLocation.seq') || '排序'" prop="seq">
          <el-input-number v-model="form.seq" :min="1" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('common.submit') }}</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
import { listShopLocations, saveShopLocation, updateShopLocation, deleteShopLocation } from '@/api/room/shopLocationApi'

export default {
  name: 'ShopLocationTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'locationName'
      },
      dialogVisible: false,
      dialogTitle: this.$t('common.add'),
      form: {
        locationId: '',
        locationName: '',
        parentId: '-1',
        level: 1,
        remark: '',
        seq: 1
      },
      rules: {
        locationName: [{ required: true, message: '请输入位置名称', trigger: 'blur' }]
      },
      isEdit: false,
      currentNode: null
    }
  },
  methods: {
    loadData() {
      this.loadTreeData()
    },
    getLocations() {
      return Promise.resolve(this.treeData)
    },
    async loadTreeData() {
      try {
        const res = await listShopLocations({ page: 1, row: 1000 })
        if (res.code === 0) {
          this.treeData = this.buildTree(res.data)
        }
      } catch (error) {
        console.error(error)
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    buildTree(list) {
      const map = {}
      const roots = []
      list.forEach(item => {
        map[item.locationId] = { ...item, children: [] }
      })
      list.forEach(item => {
        if (item.parentId && item.parentId !== '-1' && map[item.parentId]) {
          map[item.parentId].children.push(map[item.locationId])
        } else {
          roots.push(map[item.locationId])
        }
      })
      return roots
    },
    handleNodeClick(data) {
      this.$emit('select', data)
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = this.$t('common.add')
      this.form = {
        locationId: '',
        locationName: '',
        parentId: '-1',
        level: 1,
        remark: '',
        seq: 1
      }
      this.dialogVisible = true
    },
    handleAddChild(data) {
      this.isEdit = false
      this.dialogTitle = this.$t('common.add')
      this.form = {
        locationId: '',
        locationName: '',
        parentId: data.locationId,
        level: data.level + 1,
        remark: '',
        seq: 1
      }
      this.dialogVisible = true
    },
    handleEdit(data) {
      this.isEdit = true
      this.dialogTitle = this.$t('common.edit')
      this.currentNode = data
      this.form = {
        locationId: data.locationId,
        locationName: data.locationName,
        parentId: data.parentId,
        level: data.level,
        remark: data.remark,
        seq: data.seq || 1
      }
      this.dialogVisible = true
    },
    async handleDelete(data) {
      try {
        await this.$confirm('确定要删除该位置吗？', '提示', {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        })
        await deleteShopLocation(data.locationId)
        this.$message.success('删除成功')
        this.loadTreeData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error(error)
          this.$message.error('删除失败')
        }
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        if (this.isEdit) {
          await updateShopLocation(this.form)
          this.$message.success('修改成功')
        } else {
          await saveShopLocation(this.form)
          this.$message.success('添加成功')
        }
        this.dialogVisible = false
        this.loadTreeData()
      } catch (error) {
        console.error(error)
        if (error !== false) {
          this.$message.error('操作失败')
        }
      }
    },
    handleDialogClose() {
      this.$refs.form.resetFields()
    },
    open() {
      this.loadTreeData()
    },
    hasPrivilege(code) {
      const privileges = JSON.parse(localStorage.getItem('privileges') || '[]')
      return privileges.some(p => p === code)
    }
  }
}
</script>

<style scoped>
.tree-card {
  max-height: 80vh;
  overflow-y: auto;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
