<template>
  <div>
    <el-tree ref="orgTree" :data="orgs" node-key="id" :props="defaultProps" default-expand-all
      :expand-on-click-node="false" @node-click="handleNodeClick"></el-tree>
  </div>
</template>

<script>
import { listOrgTree } from '@/api/work/repairTypeUserApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OrgTreeShow',
  data() {
    return {
      orgs: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  methods: {
    async refreshTree() {
      try {
        const communityId = await getCommunityId()
        const params = {
          communityId: communityId
        }
        const response = await listOrgTree(params)
        if (response.code === 0) {
          this.orgs = [response.data]
        } else {
          this.$message.error(response.msg || this.$t('common.requestFailed'))
        }
      } catch (error) {
        console.error('Error loading org tree:', error)
        this.$message.error(this.$t('common.requestFailed'))
      }
    },
    handleNodeClick(data) {
      this.$emit('org-selected', {
        orgId: data.id,
        orgName: data.text
      })
    }
  },
  mounted() {
    this.refreshTree()
  }
}
</script>