<template>
  <div class="owner-committee-detail-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('ownerCommitteeDetail.title') }}</span>
            <div class="card-header-actions">
              <el-button type="primary" icon="el-icon-close" @click="goBack">
                {{ $t('common.back') }}
              </el-button>
            </div>
          </div>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.name') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.name }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.sex') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.sex == 'B' ? $t('common.male') : $t('common.female') }}</span>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.link') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.link }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.idCard') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.idCard }}</span>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.address') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.address }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.position') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.position }}</span>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.post') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.post }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.postDesc') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.postDesc }}</span>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.appointTime') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.appointTime }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.curTime') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.curTime }}</span>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.state') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.state == '1000' ? $t('addOwnerCommittee.onJob') : $t('addOwnerCommittee.leaveJob')
                    }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="detail-item">
                    <label>{{ $t('ownerCommitteeDetail.remark') }}:</label>
                    <span>{{ ownerCommitteeDetailInfo.remark }}</span>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('ownerCommitteeDetail.emergencyContacts') }}</span>
          </div>
          <el-table :data="ownerCommitteeDetailInfo.contracts" border style="width: 100%" v-loading="loading">
            <el-table-column prop="relName" :label="$t('ownerCommitteeDetail.memberRelation')" align="center" />
            <el-table-column prop="name" :label="$t('ownerCommitteeDetail.name')" align="center" />
            <el-table-column prop="link" :label="$t('ownerCommitteeDetail.contactPhone')" align="center" />
            <el-table-column prop="address" :label="$t('ownerCommitteeDetail.address')" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getOwnerCommitteeDetail, listOwnerCommitteeContracts } from '@/api/owner/ownerCommitteeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerCommitteeDetailList',
  data() {
    return {
      ownerCommitteeDetailInfo: {
        ocId: '',
        name: '',
        sex: '',
        link: '',
        idCard: '',
        address: '',
        position: '',
        post: '',
        postDesc: '',
        appointTime: '',
        curTime: '',
        state: '',
        remark: '',
        contracts: [],
        communityId: ''
      },
      loading: false
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      this.ownerCommitteeDetailInfo.ocId = this.$route.query.ocId
      this.communityId = getCommunityId()
      this.getOwnerCommitteeDetail()
      this.getOwnerCommitteeContracts()
    },
    async getOwnerCommitteeDetail() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          ocId: this.ownerCommitteeDetailInfo.ocId
        }
        const res = await getOwnerCommitteeDetail(params)
        if (res.data && res.data.length > 0) {
          Object.assign(this.ownerCommitteeDetailInfo, res.data[0])
        }
      } catch (error) {
        this.$message.error(this.$t('ownerCommitteeDetail.fetchDetailError'))
      } finally {
        this.loading = false
      }
    },
    async getOwnerCommitteeContracts() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 100, // 获取所有紧急联系人
          communityId: this.communityId,
          ocId: this.ownerCommitteeDetailInfo.ocId
        }
        const res = await listOwnerCommitteeContracts(params)
        this.ownerCommitteeDetailInfo.contracts = res.data || []
      } catch (error) {
        this.$message.error(this.$t('ownerCommitteeDetail.fetchContactsError'))
      } finally {
        this.loading = false
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.owner-committee-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-header-actions {
      display: flex;
      align-items: center;
    }
  }

  .detail-item {
    margin-bottom: 15px;
    display: flex;
    justify-content: flex-start;
    text-align: left;

    label {
      min-width: 100px;
      text-align: right;
      margin-right: 10px;
      color: #606266;
    }

    span {
      flex: 1;
    }
  }

  .mt-20 {
    margin-top: 20px;
  }
}
</style>