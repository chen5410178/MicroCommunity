<template>
  <div>
    <el-table :data="resources" border style="width: 100%">
      <el-table-column prop="resId" :label="$t('repairDetailResource.itemCode')" align="center" />
      <el-table-column :label="$t('repairDetailResource.itemType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
        </template>
      </el-table-column>
      <el-table-column prop="resourceStoreName" :label="$t('repairDetailResource.itemName')" align="center" />
      <el-table-column :label="$t('repairDetailResource.specification')" align="center">
        <template slot-scope="scope">
          {{ scope.row.specName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('repairDetailResource.quantity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.quantity }}{{ scope.row.miniUnitCodeName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('repairDetailResource.price')" align="center">
        <template slot-scope="scope">
          {{ scope.row.unitPrice || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" :label="$t('repairDetailResource.user')" align="center" />
      <el-table-column prop="createTime" :label="$t('repairDetailResource.time')" align="center" />
    </el-table>
  </div>
</template>

<script>
import { getRepairResources } from '@/api/work/repairDetailApi'

export default {
  name: 'RepairDetailResource',
  props: {
    repairId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      resources: []
    }
  },
  methods: {
    async loadData() {
      try {
        this.resources = await getRepairResources(this.repairId)
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>