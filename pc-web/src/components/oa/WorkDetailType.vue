<template>
  <div class="work-detail-type">
    <div class="margin-top">
      <el-table
        :data="workDetailTypeInfo.types"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="typeName"
          :label="$t('workDetailType.typeName')"
          align="center"
        />
        <el-table-column
          :label="$t('workDetailType.notifyWay')"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.smsWay === 'WECHAT'">{{ $t('workDetailType.wechat') }}</span>
            <span v-else-if="scope.row.smsWay === 'ALI_SMS'">{{ $t('workDetailType.aliSms') }}</span>
            <span v-else>{{ $t('workDetailType.unknown') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('workDetailType.createTime')"
          align="center"
        />
        <el-table-column
          prop="remark"
          :label="$t('workDetailType.remark')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { listWorkType } from '@/api/oa/workDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WorkDetailType',
  data() {
    return {
      workDetailTypeInfo: {
        types: [],
        wtId: ''
      },
      loading: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.workDetailTypeInfo.wtId = params.wtId
      this.loadWorkDetailTypeData(this.page.current, this.page.size)
    },
    async loadWorkDetailTypeData(page, size) {
      try {
        this.loading = true
        const params = {
          wtId: this.workDetailTypeInfo.wtId,
          communityId: this.communityId,
          page,
          row: size
        }
        const { data, total } = await listWorkType(params)
        this.workDetailTypeInfo.types = data
        this.page.total = total
      } catch (error) {
        console.error('获取工作单类型列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadWorkDetailTypeData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadWorkDetailTypeData(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>