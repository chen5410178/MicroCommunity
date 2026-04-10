<template>
  <div class="property-right-detail-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
          <div>
            <span>{{ listPropertyRightRegistrationDetailInfo.conditions.floorNum }}-{{
              listPropertyRightRegistrationDetailInfo.conditions.unitNum }}-{{
                listPropertyRightRegistrationDetailInfo.conditions.roomNum }}</span>
            <span>{{ $t('propertyRightDetail.title') }}</span>
          </div>
          <div class="card-tools">
            <el-button type="primary" size="small" @click="_goBack">
              <i class="el-icon-close"></i>
              <span>{{ $t('common.back') }}</span>
            </el-button>
          </div>
     
      </div>

      <el-table :data="listPropertyRightRegistrationDetailInfo.propertyRightRegistrationDetails" border
        style="width: 100%" v-loading="loading">
        <el-table-column prop="prrdId" :label="$t('propertyRightDetail.table.id')" align="center" />
        <el-table-column prop="securitiesName" :label="$t('propertyRightDetail.table.materialType')" align="center" />
        <el-table-column :label="$t('propertyRightDetail.table.isPayment')" align="center">
          <template slot-scope="scope">
            {{ scope.row.isTrue === '-1' ? '--' : scope.row.isTrue === 'true' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('propertyRightDetail.table.image')" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.securities === '001'">
              <div v-for="(item, index) in scope.row.idCardUrlShow" :key="index" class="image-container"
                >
                <el-image :src="item" :preview-src-list="scope.row.idCardUrlShow" fit="cover"
                  style="width: 50px; height: 50px">
                  <div slot="error" class="image-slot">
                    <img src="/img/noPhoto.jpg" style="width: 50px; height: 50px">
                  </div>
                </el-image>
                <img src="/img/icon-bigimg.png" class="preview-icon">
              </div>
            </div>
            <div v-else-if="scope.row.securities === '002'">
              <div v-for="(item, index) in scope.row.housePurchaseUrlShow" :key="index" class="image-container"
               >
                <el-image :src="item" :preview-src-list="scope.row.housePurchaseUrlShow" fit="cover"
                  style="width: 50px; height: 50px" >
                  <div slot="error" class="image-slot">
                    <img src="/img/noPhoto.jpg" style="width: 50px; height: 50px">
                  </div>
                </el-image>
                <img src="/img/icon-bigimg.png" class="preview-icon">
              </div>
            </div>
            <div v-else-if="scope.row.securities === '003'">
              <div v-for="(item, index) in scope.row.repairUrlShow" :key="index" class="image-container"
                >
                <el-image :src="item" :preview-src-list="scope.row.repairUrlShow" fit="cover"
                  style="width: 50px; height: 50px" >
                  <div slot="error" class="image-slot">
                    <img src="/img/noPhoto.jpg" style="width: 50px; height: 50px">
                  </div>
                </el-image>
                <img src="/img/icon-bigimg.png" class="preview-icon">
              </div>
            </div>
            <div v-else-if="scope.row.securities === '004'">
              <div v-for="(item, index) in scope.row.deedTaxUrlShow" :key="index" class="image-container"
                  >
                <el-image :src="item" :preview-src-list="scope.row.deedTaxUrlShow" fit="cover"
                  style="width: 50px; height: 50px" >
                  <div slot="error" class="image-slot">
                    <img src="/img/noPhoto.jpg" style="width: 50px; height: 50px">
                  </div>
                </el-image>
                <img src="/img/icon-bigimg.png" class="preview-icon">
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('propertyRightDetail.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_openEditPropertyRightRegistrationDetailModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <edit-property-right-registration-detail ref="editPropertyRightRegistrationDetail" @success="handleSuccess" />
  </div>
</template>

<script>
import { listPropertyRightRegistrationDetail } from '@/api/room/listPropertyRightRegistrationDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
import EditPropertyRightRegistrationDetail from '@/components/room/editPropertyRightRegistrationDetail'

export default {
  name: 'ListPropertyRightRegistrationDetail',
  components: {
    EditPropertyRightRegistrationDetail,
  },
  data() {
    return {
      loading: false,
      listPropertyRightRegistrationDetailInfo: {
        propertyRightRegistrationDetails: [],
        conditions: {
          prrId: '',
          securities: '',
          floorNum: '',
          unitNum: '',
          roomNum: '',
          isTrue: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.listPropertyRightRegistrationDetailInfo.conditions.communityId = getCommunityId()
    this.listPropertyRightRegistrationDetailInfo.conditions.prrId = this.$route.query.prrId
    this.listPropertyRightRegistrationDetailInfo.conditions.floorNum = this.$route.query.floorNum
    this.listPropertyRightRegistrationDetailInfo.conditions.unitNum = this.$route.query.unitNum
    this.listPropertyRightRegistrationDetailInfo.conditions.roomNum = this.$route.query.roomNum
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.listPropertyRightRegistrationDetailInfo.conditions
        }
        const { data, total } = await listPropertyRightRegistrationDetail(params)
        this.listPropertyRightRegistrationDetailInfo.propertyRightRegistrationDetails = data.map(item => {
          if (item.securities === '001' && item.idCardUrl) {
            item.idCardUrl = item.idCardUrl.split(',')
            item.idCardUrlShow = item.idCardUrl
          }
          if (item.securities === '002' && item.housePurchaseUrl) {
            item.housePurchaseUrl = item.housePurchaseUrl.split(',')
            item.housePurchaseUrlShow = item.housePurchaseUrl
          }
          if (item.securities === '003' && item.repairUrl) {
            item.repairUrl = item.repairUrl.split(',')
            item.repairUrlShow = item.repairUrl
          }
          if (item.securities === '004' && item.deedTaxUrl) {
            item.deedTaxUrl = item.deedTaxUrl.split(',')
            item.deedTaxUrlShow = item.deedTaxUrl
          }
          return item
        })
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('propertyRightDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    _openEditPropertyRightRegistrationDetailModel(row) {
      this.$refs.editPropertyRightRegistrationDetail.open(row)
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.property-right-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;

      span {
        font-size: 18px;
        font-weight: bold;
      }
    }
  }

  .image-container {
    position: relative;
    display: inline-block;
    margin-right: 10px;

    .preview-icon {
      position: absolute;
      right: 0;
      bottom: 0;
      width: 20px;
      height: 20px;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>