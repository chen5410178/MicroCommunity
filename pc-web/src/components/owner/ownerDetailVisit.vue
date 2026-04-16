<template>
  <div class="margin-top">
    <el-table :data="ownerDetailVisitInfo.visits" style="width: 100%">
      <el-table-column prop="vId" :label="$t('visitManage.table.visitId')" align="center"></el-table-column>
      <el-table-column :label="$t('visitManage.table.photo')" align="center">
        <template slot-scope="scope">
          <div style="position: relative; display: inline-block;" 
               @click="showVisitImg(scope.row.url?scope.row.url:'/img/noPhoto.jpg')">
            <img width="50" height="50" :src="scope.row.url?scope.row.url:'/img/noPhoto.jpg'"
                 onerror="this.src='/img/noPhoto.jpg';">
            <img src="/img/icon-bigimg.png" style="position: absolute;right: 0;bottom: 0;" width="50"
                 height="50" alt="">
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('visitManage.table.name')" align="center">
        <template slot-scope="scope">
          {{scope.row.vName}}({{scope.row.phoneNumber}})/{{scope.row.visitGender=='0'?$t('common.male'):$t('common.female')}}
        </template>
      </el-table-column>
      <el-table-column prop="ownerName" :label="$t('visitManage.table.ownerName')" align="center"></el-table-column>
      <el-table-column :label="$t('visitManage.table.visitCase')" align="center">
        <template slot-scope="scope">
          {{scope.row.visitCase}}({{scope.row.reasonTypeName}})
        </template>
      </el-table-column>
      <el-table-column :label="$t('visitManage.table.carNum')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.carState == '1' && scope.row.psId != null && scope.row.psId != '' && scope.row.psId != undefined">
            {{scope.row.carNum}}<br />({{scope.row.parkAreaNum}}{{$t('visitManage.table.parkingLot')}}-{{scope.row.parkingSpaceNum}}{{$t('visitManage.table.parkingSpace')}})
          </span>
          <span v-else>{{scope.row.carNum}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="entourage" :label="$t('visitManage.table.entourage')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('visitManage.table.createTime')" align="center"></el-table-column>
      <el-table-column :label="$t('visitManage.table.visitTime')" align="center">
        <template slot-scope="scope">
          {{scope.row.visitTime}}<br />{{scope.row.departureTime}}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('visitManage.table.state')" align="center"></el-table-column>
      <el-table-column prop="carStateName" :label="$t('visitManage.table.carStatus')" align="center"></el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="300">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" @click="_openVisitDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button size="mini" v-if="scope.row.state != '1' && scope.row.state != '2'" 
                       @click="_openEditVisitModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" v-if="scope.row.carNum != '' && scope.row.carState == '0'" 
                       @click="_openExamineVisitCarModel(scope.row)">
              {{ $t('common.audit') }}
            </el-button>
            <el-button size="mini" @click="_openDeleteVisitModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      layout="total, prev, pager, next, jumper">
    </el-pagination>

    <edit-visit ref="editVisit" @refresh="_loadOwnerDetailVisitData"></edit-visit>
    <examine-visit-car ref="examineVisitCar" @refresh="_loadOwnerDetailVisitData"></examine-visit-car>
    <delete-visit ref="deleteVisit" @refresh="_loadOwnerDetailVisitData"></delete-visit>
    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
//import EditVisit from '@/components/oa/editVisit'
//import ExamineVisitCar from '@/components/oa/examineVisitCar'
//import DeleteVisit from '@/components/oa/deleteVisit'
import ViewImage from '@/components/system/viewImage'
import { listVisits } from '@/api/owner/ownerDetailVisitApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailVisit',
  components: {
    //EditVisit,
    //ExamineVisitCar,
    //DeleteVisit,
    ViewImage
  },
  data() {
    return {
      ownerDetailVisitInfo: {
        visits: [],
        ownerId: '',
        link: '',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    _loadOwnerDetailVisitData(page, row) {
      const params = {
        page: page,
        row: row,
        communityId: getCommunityId(),
        ownerTel: this.ownerDetailVisitInfo.link,
        channel: 'PC'
      }

      listVisits(params).then(response => {
        this.ownerDetailVisitInfo.visits = response.visits
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    showVisitImg(url) {
      this.$refs.viewImage.open(url || '/img/noPhoto.jpg')
    },
    _openVisitDetail(item) {
      const flowId = item.flowId || ''
      this.$router.push(`/property/visitDetail?vId=${item.vId}&flowId=${flowId}`)
    },
    _openEditVisitModel(visit) {
      this.$refs.editVisit.open(visit)
    },
    _openExamineVisitCarModel(visit) {
      this.$refs.examineVisitCar.open(visit)
    },
    _openDeleteVisitModel(visit) {
      this.$refs.deleteVisit.open(visit)
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailVisitData(val, this.pagination.pageSize)
    }
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailVisitInfo.ownerId = data.ownerId
      this.ownerDetailVisitInfo.link = data.link
      this._loadOwnerDetailVisitData(1, this.pagination.pageSize)
    })
  }
}
</script>