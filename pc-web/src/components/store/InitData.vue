<template>
  <div></div>
</template>

<script>
import { listMyEnteredCommunitys, queryStoreByUser, queryUserPrivilege } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'InitData',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    loginUsers: {
      type: Array,
      default: () => []
    },
    pageUrl: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      selectedUser: null
    }
  },
  methods: {
    async initData() {
      if (this.loginUsers.length > 1) {
        this.showUserSelection()
      } else {
        await this.handleUserSelected(this.loginUsers[0])
      }
    },
    showUserSelection() {
      this.$confirm({
        title: this.$t('propertyCompanyManage.selectAccount'),
        message: this.renderUserTable(),
        showCancelButton: true,
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel')
      }).then(() => {
        if (this.selectedUser) {
          this.handleUserSelected(this.selectedUser)
        } else {
          this.$message.warning(this.$t('propertyCompanyManage.selectUserFirst'))
        }
      })
    },
    renderUserTable() {
      return (
        <div style="max-height: 300px; overflow-y: auto;">
          <el-table data={this.loginUsers} on-selection-change={this.handleSelectionChange}>
            <el-table-column type="selection" width="55" />
            <el-table-column property="userName" label={this.$t('propertyCompanyManage.account')} />
            <el-table-column property="storeName" label={this.$t('propertyCompanyManage.belongTo')} />
            <el-table-column property="relCdName" label={this.$t('propertyCompanyManage.role')} />
          </el-table>
        </div>
      )
    },
    handleSelectionChange(val) {
      this.selectedUser = val.length > 0 ? val[0] : null
    },
    async handleUserSelected(user) {
      try {
        // 保存token
        this.$store.dispatch('user/setToken', user.token)
        
        // 验证是否有店铺
        await queryStoreByUser({ _uId: 'ccdd00opikookjuhyyttvhnnjuuu' })
        
        // 加载权限
        const { datas } = await queryUserPrivilege({ a: 'HC' })
        const privileges = datas.map(item => item.pId)
        this.$store.dispatch('user/setPrivileges', privileges)
        
        // 加载小区信息
        const { communitys } = await listMyEnteredCommunitys({
          _uId: 'ccdd00opikookjuhyyttvhnnjuuu',
          page: 1,
          row: 3
        })
        
        if (communitys && communitys.length > 0) {
          this.$store.dispatch('community/setCurrentCommunity', communitys[0])
          this.$store.dispatch('community/setCommunitys', communitys)
          this.$router.push(this.pageUrl)
        } else {
          this.$message.error(this.$t('propertyCompanyManage.noCommunityAssigned'))
        }
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  },
  mounted() {
    this.initData()
  }
}
</script>