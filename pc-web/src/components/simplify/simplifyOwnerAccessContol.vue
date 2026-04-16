<template>
  <div>
    <el-table
      :data="simplifyOwnerAccessContolInfo.machineTranslates"
      style="width: 100%; margin-top: 10px"
      border>
      <el-table-column prop="typeCdName" :label="$t('simplifyOwnerAccessContol.objectType')" align="center"></el-table-column>
      <el-table-column prop="objName" :label="$t('simplifyOwnerAccessContol.objectName')" align="center"></el-table-column>
      <el-table-column prop="machineCmdName" :label="$t('simplifyOwnerAccessContol.command')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyOwnerAccessContol.status')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('simplifyOwnerAccessContol.remark')" align="center"></el-table-column>
      <el-table-column prop="updateTime" :label="$t('simplifyOwnerAccessContol.syncTime')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerAccessContol.operation')" align="center">
        <template #default="{row}">
          <el-button 
            v-if="row.state != '20000'" 
            type="text" 
            @click="_openEditMachineTranslateModel(row)">
            {{ $t('simplifyOwnerAccessContol.resync') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>

    <edit-machine-translate ref="editMachineTranslate" @listMachineTranslate="listSimplifyOwnerAccessContol"></edit-machine-translate>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listMachineTranslates } from '@/api/simplify/simplifyOwnerAccessContolApi'
import EditMachineTranslate from '@/components/machine/editMachineTranslate'

export default {
  name: 'SimplifyOwnerAccessContol',
  components: {
    EditMachineTranslate
  },
  data() {
    return {
      simplifyOwnerAccessContolInfo: {
        machineTranslates: [],
        ownerId: '',
        roomId: '',
        total: 0,
        records: 1
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(param) {
      this.handleSwitch(param)
    },
    handleSwitch(param) {
      if (param.ownerId == '') return
      this.clearSimplifyOwnerAccessContolInfo()
      Object.assign(this.simplifyOwnerAccessContolInfo, param)
      this.listSimplifyOwnerAccessContol(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listSimplifyOwnerAccessContol(val, this.pageSize)
    },
    listSimplifyOwnerAccessContol(page, row) {
      const params = {
        page,
        row,
        communityId: this.communityId,
        objId: this.simplifyOwnerAccessContolInfo.ownerId,
        typeCd: '8899'
      }
      
      listMachineTranslates(params).then(res => {
        this.simplifyOwnerAccessContolInfo.machineTranslates = res.machineTranslates
        this.total = res.total
      })
    },
    _openEditMachineTranslateModel(machineTranslate) {
      this.$refs.editMachineTranslate.open(machineTranslate)
    },
    clearSimplifyOwnerAccessContolInfo() {
      this.simplifyOwnerAccessContolInfo = {
        machineTranslates: [],
        ownerId: '',
        roomId: ''
      }
    }
  }
}
</script>

<style scoped>
</style>