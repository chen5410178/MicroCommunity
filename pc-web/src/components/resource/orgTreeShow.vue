<template>
  <div class="org-tree-show-container">
    <el-tree
      :data="orgs"
      :props="defaultProps"
      node-key="id"
      default-expand-all
      highlight-current
      @node-click="handleNodeClick"
    ></el-tree>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OrgTreeShow',
  props: {
    callBackListener: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      orgs: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  created() {
    this.loadOrgs()
  },
  methods: {
    async loadOrgs() {
      try {
        const params = {
          communityId: getCommunityId()
        }

        const res = await this.$api.resource.transferGoodsManageApi.listOrgTree(
          params
        )
        this.orgs = res.data
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    handleNodeClick(data) {
      if (this.callBackListener) {
        this.$emit(this.callBackListener, 'switchOrg', {
          orgId: data.id,
          orgName: data.text
        })
      }
    },
    refreshTree() {
      this.loadOrgs()
    }
  }
}
</script>

<style scoped>
.org-tree-show-container {
  padding: 10px;
}
</style>