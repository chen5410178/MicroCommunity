<template>
  <el-dialog
    :title="$t('scheduleClassesPage.selectOrg')"
    :visible.sync="dialogVisible"
    width="40%"
  >
    <el-tree
      ref="orgTree"
      :data="chooseOrgInfo.orgs"
      :props="defaultProps"
      node-key="id"
      highlight-current
      @node-click="handleNodeClick"
    />
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="_doChooseOrg">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOrgTree } from '@/api/org/orgApi'

export default {
  name: 'ChooseOrgTree',
  data() {
    return {
      dialogVisible: false,
      chooseOrgInfo: {
        orgs: [],
        orgId: '',
        curOrg: {}
      },
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this._loadChooseOrgs()
    },
    async _loadChooseOrgs() {
      try {
        const params = {
          communityId: getCommunityId()
        }
        const { data } = await listOrgTree(params)
        
        // 确保数据是数组格式
        let treeData = [data]
        
        // 如果返回的是对象而不是数组，尝试提取数组
        if (Array.isArray(treeData)) {
          this.chooseOrgInfo.orgs = treeData
        } else if (treeData && Array.isArray(treeData.children)) {
          this.chooseOrgInfo.orgs = treeData.children
        } else if (treeData && Array.isArray(treeData.data)) {
          this.chooseOrgInfo.orgs = treeData.data
        } else if (treeData && Array.isArray(treeData.list)) {
          this.chooseOrgInfo.orgs = treeData.list
        } else {
          // 如果都不是数组，转换为数组格式
          this.chooseOrgInfo.orgs = Array.isArray(treeData) ? treeData : [treeData]
        }
        
        // 确保每个节点都有 children 属性且为数组
        this.processTreeData(this.chooseOrgInfo.orgs)
      } catch (error) {
        console.error('获取组织树失败:', error)
        this.chooseOrgInfo.orgs = []
      }
    },
    processTreeData(nodes) {
      if (!Array.isArray(nodes)) return
      
      nodes.forEach(node => {
        // 确保每个节点都有 children 属性且为数组
        if (!node.children) {
          node.children = []
        } else if (!Array.isArray(node.children)) {
          node.children = []
        }
        
        // 递归处理子节点
        if (node.children && node.children.length > 0) {
          this.processTreeData(node.children)
        }
      })
    },
    handleNodeClick(data) {
      this.chooseOrgInfo.curOrg = data
      this.chooseOrgInfo.curOrg.orgId = data.id
    },
    _doChooseOrg() {
      this.$emit('switchOrg', this.chooseOrgInfo.curOrg)
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog__body {
  padding: 20px;
  max-height: 60vh;
  overflow-y: auto;
}
</style>