<template>
  <div class="margin-top">
    <el-table
      :data="ownerDetailAccessControlInfo.machineTranslates"
      style="width: 100%"
      border
      stripe
    >
      <el-table-column prop="typeCdName" :label="$t('ownerDetailAccessControl.objectType')" align="center"></el-table-column>
      <el-table-column prop="objName" :label="$t('ownerDetailAccessControl.objectName')" align="center"></el-table-column>
      <el-table-column prop="machineCmdName" :label="$t('ownerDetailAccessControl.command')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('ownerDetailAccessControl.status')" align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailAccessControl.description')" align="center">
        <template slot-scope="scope">
          <div class="hc-td">{{scope.row.remark}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" :label="$t('ownerDetailAccessControl.syncTime')" align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailAccessControl.operation')" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.state != '20000'"
            type="text"
            size="small"
            @click="_openEditMachineTranslateModel(scope.row)"
          >
            {{$t('ownerDetailAccessControl.resync')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row>
      <el-col :span="4"></el-col>
      <el-col :span="20">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-col>
    </el-row>
    <edit-machine-translate ref="editMachineTranslate"></edit-machine-translate>
  </div>
</template>

<script>
import { listMachineTranslates } from '@/api/owner/ownerDetailAccessControlApi'
import { getCommunityId } from '@/api/community/communityApi'
import EditMachineTranslate from '@/components/machine/editMachineTranslate'

export default {
  name: 'OwnerDetailAccessControl',
  components: {
    EditMachineTranslate
  },
  data() {
    return {
      ownerDetailAccessControlInfo: {
        machineTranslates: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(ownerId) {
      this.ownerDetailAccessControlInfo.ownerId = ownerId
      this._loadOwnerDetailAccessControlData(this.currentPage, this.pageSize)
    },
    _loadOwnerDetailAccessControlData(page, row) {
      const param = {
        page: page,
        row: row,
        communityId: this.communityId,
        objId: this.ownerDetailAccessControlInfo.ownerId,
        typeCd: '8899'
      }

      listMachineTranslates(param).then(response => {
        this.ownerDetailAccessControlInfo.machineTranslates = response.data.machineTranslates
        this.total = response.data.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _openEditMachineTranslateModel(machineTranslate) {
      this.$refs.editMachineTranslate.open(machineTranslate)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadOwnerDetailAccessControlData(val, this.pageSize)
    }
  }
}
</script>