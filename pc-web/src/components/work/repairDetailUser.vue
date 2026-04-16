<template>
  <div>
    <el-table :data="users" border style="width: 100%">
      <el-table-column :label="$t('repairDetailUser.serial')" align="center" width="80">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="staffName" :label="$t('repairDetailUser.handler')" align="center" />
      <el-table-column :label="$t('repairDetailUser.status')" align="center">
        <template slot-scope="scope">
          <template v-if="(scope.row.state === '10009' || scope.row.state === '12000') && scope.row.payTypeName">
            {{ scope.row.stateName }}({{ scope.row.payTypeName }})
          </template>
          <template v-else>
            {{ scope.row.stateName }}
            <span v-if="scope.row.state === '10007'">
              (<a href="javascript:void(0)" @click="openRepairAppraise(scope.row)">{{ $t('repairDetailUser.reply') }}</a>)
            </span>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" :label="$t('repairDetailUser.startTime')" align="center" />
      <el-table-column prop="endTime" :label="$t('repairDetailUser.endTime')" align="center" />
      <el-table-column prop="duration" :label="$t('repairDetailUser.duration')" align="center" />
      <el-table-column prop="context" :label="$t('repairDetailUser.opinion')" align="center" />
    </el-table>
  </div>
</template>

<script>
import { getRepairFlow } from '@/api/work/repairDetailApi'

export default {
  name: 'RepairDetailUser',
  props: {
    repairId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      users: []
    }
  },
  methods: {
    async loadData() {
      try {
        this.users = await getRepairFlow(this.repairId)
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    openRepairAppraise(row) {
      this.$emit('open-appraise', row)
    }
  }
}
</script>