<template>
  <div class="admin-repair-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminRepairDetail.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="goBack">
          {{ $t('adminRepairDetail.back') }}
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.repairId') }}：</label>
            <span>{{ adminRepairDetailInfo.repairId }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.repairType') }}：</label>
            <span>{{ adminRepairDetailInfo.repairTypeName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.repairName') }}：</label>
            <span>{{ adminRepairDetailInfo.repairName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.tel') }}：</label>
            <span>{{ adminRepairDetailInfo.tel }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.location') }}：</label>
            <span>{{ adminRepairDetailInfo.repairObjName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.appointmentTime') }}：</label>
            <span>{{ adminRepairDetailInfo.appointmentTime }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.status') }}：</label>
            <span>{{ adminRepairDetailInfo.stateName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('adminRepairDetail.repairContent') }}：</label>
            <span>{{ adminRepairDetailInfo.context }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 其他信息展示部分，按照相同模式转换 -->
    <!-- 图片展示部分 -->
    <el-card v-if="adminRepairDetailInfo.repairPhotos.length > 0" class="box-card margin-top">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminRepairDetail.repairPhotos') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col v-for="(item, index) in adminRepairDetailInfo.repairPhotos" :key="index" :span="4">
          <el-image style="width: 120px; height: 120px;" :src="item.url" :preview-src-list="[item.url]" fit="cover" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 维修前图片 -->
    <el-card v-if="adminRepairDetailInfo.beforePhotos.length > 0" class="box-card margin-top">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminRepairDetail.beforePhotos') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col v-for="(item, index) in adminRepairDetailInfo.beforePhotos" :key="index" :span="4">
          <el-image style="width: 120px; height: 120px;" :src="item.url" :preview-src-list="[item.url]" fit="cover" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 维修后图片 -->
    <el-card v-if="adminRepairDetailInfo.afterPhotos.length > 0" class="box-card margin-top">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminRepairDetail.afterPhotos') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col v-for="(item, index) in adminRepairDetailInfo.afterPhotos" :key="index" :span="4">
          <el-image style="width: 120px; height: 120px;" :src="item.url" :preview-src-list="[item.url]" fit="cover" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 相关物品表格 -->
    <el-card v-if="adminRepairDetailInfo.maintenanceType === '1001' || adminRepairDetailInfo.maintenanceType === '1003'"
      class="box-card margin-top">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminRepairDetail.relatedItems') }}</span>
      </div>
      <el-table :data="adminRepairDetailInfo.resourceStoreInfo" border>
        <el-table-column prop="resId" :label="$t('adminRepairDetail.itemId')" align="center" />
        <el-table-column :label="$t('adminRepairDetail.itemType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
          </template>
        </el-table-column>
        <el-table-column prop="resourceStoreName" :label="$t('adminRepairDetail.itemName')" align="center" />
        <el-table-column prop="specName" :label="$t('adminRepairDetail.itemSpec')" align="center">
          <template slot-scope="scope">
            {{ scope.row.specName || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('adminRepairDetail.itemQuantity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.quantity }}{{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column prop="unitPrice" :label="$t('adminRepairDetail.itemPrice')" align="center">
          <template slot-scope="scope">
            {{ scope.row.unitPrice || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createUserName" :label="$t('adminRepairDetail.user')" align="center" />
        <el-table-column prop="createTime" :label="$t('adminRepairDetail.time')" align="center" />
      </el-table>
    </el-card>

    <!-- 工单流转表格 -->
    <el-card class="box-card margin-top">
      <div slot="header" class="clearfix">
        <span>{{ $t('adminRepairDetail.workflow') }}</span>
      </div>
      <el-table :data="adminRepairDetailInfo.repairUsers" border>
        <el-table-column type="index" :label="$t('adminRepairDetail.serial')" align="center" width="60" />
        <el-table-column prop="staffName" :label="$t('adminRepairDetail.handler')" align="center" />
        <el-table-column :label="$t('adminRepairDetail.status')" align="center">
          <template slot-scope="scope">
            <span v-if="(scope.row.state === '10009' || scope.row.state === '12000') && scope.row.payTypeName">
              {{ scope.row.stateName }}({{ scope.row.payTypeName }})
            </span>
            <span v-else>
              {{ scope.row.stateName }}
              <span v-if="scope.row.state === '10007'">
                (<el-link type="primary" @click="openRepairAppraise(scope.row)">{{ $t('adminRepairDetail.reply')
                }}</el-link>)
              </span>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('adminRepairDetail.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('adminRepairDetail.endTime')" align="center" />
        <el-table-column prop="duration" :label="$t('adminRepairDetail.duration')" align="center" />
        <el-table-column prop="context" :label="$t('adminRepairDetail.comment')" align="center" />
      </el-table>
    </el-card>


    <reply-repair-appraise ref="replyRepairAppraise" @success="loadRepairUser" />
  </div>
</template>

<script>
import { getRepairDetail, listRepairStaffs, listStoreUseRecords } from '@/api/work/adminRepairDetailApi'
import ReplyRepairAppraise from '@/components/work/ReplyRepairAppraise'

export default {
  name: 'AdminRepairDetail',
  components: {
    ReplyRepairAppraise
  },
  data() {
    return {
      adminRepairDetailInfo: {
        repairId: '',
        repairType: '',
        repairTypeName: '',
        repairName: '',
        tel: '',
        roomId: '',
        roomName: '',
        repairObjName: '',
        appointmentTime: '',
        context: '',
        stateName: '',
        userId: '',
        userName: '',
        repairUsers: [],
        photos: [],
        repairPhotos: [],
        beforePhotos: [],
        afterPhotos: [],
        visitType: '',
        visitContext: '',
        maintenanceType: '',
        repairMaterials: '',
        repairFee: '',
        resourceStoreInfo: [],
        appraiseScore: 0,
        doorSpeedScore: 0,
        repairmanServiceScore: 0,
        average: 0.0
      }
    }
  },
  created() {
    const repairId = this.$route.query.repairId
    if (!repairId) {
      this.$message.error(this.$t('adminRepairDetail.invalidOperation'))
      this.$router.push('/work/repairPoolManage')
      return
    }
    this.adminRepairDetailInfo.repairId = repairId
    this.loadRepairDetail()
  },
  methods: {
    async loadRepairDetail() {
      try {
        const params = {
          page: 1,
          row: 1,
          repairId: this.adminRepairDetailInfo.repairId
        }
        const { data } = await getRepairDetail(params)
        if (data.length < 1) {
          this.$message.error(this.$t('adminRepairDetail.dataError'))
          this.$router.push('/work/repairPoolManage')
          return
        }
        this.adminRepairDetailInfo = { ...this.adminRepairDetailInfo, ...data[0] }

        if (this.adminRepairDetailInfo.maintenanceType === '1001' || this.adminRepairDetailInfo.maintenanceType === '1003') {
          this.loadResourceStoreList()
        }
        this.loadRepairUser()
      } catch (error) {
        this.$message.error(error.message || this.$t('adminRepairDetail.loadError'))
      }
    },
    async loadResourceStoreList() {
      try {
        const params = {
          page: 1,
          row: 100,
          repairId: this.adminRepairDetailInfo.repairId
        }
        const { data } = await listStoreUseRecords(params)
        this.adminRepairDetailInfo.resourceStoreInfo = data.map(item => {
          if (item.resId === '666666') {
            return { ...item, rstName: '自定义', specName: '自定义' }
          }
          return item
        })
      } catch (error) {
        this.$message.error(error.message || this.$t('adminRepairDetail.loadResourceError'))
      }
    },
    async loadRepairUser() {
      try {
        const params = {
          page: 1,
          row: 100,
          repairId: this.adminRepairDetailInfo.repairId
        }
        const { data } = await listRepairStaffs(params)
        this.adminRepairDetailInfo.repairUsers = data
      } catch (error) {
        this.$message.error(error.message || this.$t('adminRepairDetail.loadUserError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    openRepairAppraise(row) {
      this.$refs.replyRepairAppraise.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-repair-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .form-item {
    margin-bottom: 15px;
    text-align: left;
    color: #666;

    label {
      margin-right: 10px;
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .float-right {
    float: right;
  }
}
</style>