<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <el-button size="mini" @click="handleAddOrg">
        {{ $t('common.add') }}
      </el-button>
      <el-button size="mini" @click="handleEditOrg">
        {{ $t('common.edit') }}
      </el-button>
      <el-button size="mini" type="danger" @click="handleDeleteOrg">
        {{ $t('common.delete') }}
      </el-button>
    </div>
    <el-tree ref="orgTree" :data="orgTreeData" :props="defaultProps" node-key="id" highlight-current
      @node-click="handleNodeClick" />
    <add-org ref="addOrg" @refresh="loadOrgs" />
    <edit-org ref="editOrg" @refresh="loadOrgs" />
    <delete-org ref="deleteOrg" @refresh="loadOrgs" />
  </el-card>
</template>
  
<script>
import { listOrgTree } from '@/api/org/orgApi'
import AddOrg from './AddOrg'
import EditOrg from './EditOrg'
import DeleteOrg from './DeleteOrg'

export default {
  name: 'OrgTree',
  components: { AddOrg, EditOrg, DeleteOrg },
  data() {
    return {
      orgTreeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      currentNode: null
    }
  },
  created() {
    this.loadOrgs()
  },
  methods: {
    loadOrgs() {
      this.orgTreeData = []
      listOrgTree({
        communityId: '-1'
      }).then(response => {
        console.log(response)
        this.orgTreeData.push(response.data)
      })
    },
    handleNodeClick(data) {
      this.currentNode = data
      this.$emit('switchOrg', {
        orgId: data.id,
        orgName: data.text
      })
    },
    handleAddOrg() {
      if (!this.currentNode) {
        this.$message.warning(this.$t('org.selectOrgFirst'))
        return
      }
      this.$refs.addOrg.show({
        parentOrgId: this.currentNode.id,
        parentOrgName: this.currentNode.text
      })
    },
    handleEditOrg() {
      if (!this.currentNode) {
        this.$message.warning(this.$t('org.selectOrgFirst'))
        return
      }
      if(this.currentNode.parentId == '-1'){
        this.$message.warning(this.$t('org.topOrg'))
        return
      }
      console.log(this.currentNode)
      this.$refs.editOrg.show(this.currentNode)
    },
    handleDeleteOrg() {
      if (!this.currentNode) {
        this.$message.warning(this.$t('org.selectOrgFirst'))
        return
      }
      this.$refs.deleteOrg.show(this.currentNode)
    }
  }
}
</script>