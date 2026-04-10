<template>
  <el-dialog
    :title="$t('itemReleaseManage.viewItems.title')"
    :visible.sync="visible"
    width="50%"
  >
    <el-table
      :data="items"
      border
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column
        prop="resName"
        :label="$t('itemReleaseManage.viewItems.itemName')"
        align="center"
      />
      <el-table-column
        prop="amount"
        :label="$t('itemReleaseManage.viewItems.amount')"
        align="center"
      />
    </el-table>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listItemReleaseRes } from '@/api/work/itemReleaseManageApi'

export default {
  name: 'ViewItemReleaseRes',
  data() {
    return {
      visible: false,
      loading: false,
      communityId: '',
      items: [],
      currentItem: {}
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(item) {
      this.currentItem = {
        ...item,
        communityId: this.communityId
      }
      this.visible = true
      this.loadItems()
    },
    async loadItems() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 500,
          communityId: this.communityId,
          irId: this.currentItem.irId
        }
        const { data } = await listItemReleaseRes(params)
        this.items = data
      } catch (error) {
        console.error('获取放行物品失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>