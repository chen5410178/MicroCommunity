<template>
  <div class="coupon-property-user-detail-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="verification-card">
          <div class="verification-wrapper">
            <el-row :gutter="10">
              <el-col :span="18">
                <el-input v-model="couponId" :placeholder="$t('couponPropertyUser.scanPlaceholder')"
                  @keyup.enter.native="handleConfirmCoupon" />
              </el-col>
              <el-col :span="6">
                <el-button type="primary" @click="handleConfirmCoupon">
                  {{ $t('couponPropertyUser.verify') }}
                </el-button>
              </el-col>
            </el-row>

            <div class="verification-result">
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.verifyResult') }}:</span>
                <span class="value">{{ order.remark }}</span>
              </div>
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.verifyTime') }}:</span>
                <span class="value">{{ order.createTime }}</span>
              </div>
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.couponName') }}:</span>
                <span class="value">{{ order.couponName }}</span>
              </div>
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.faceValue') }}:</span>
                <span class="value">{{ order.value }}</span>
              </div>
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.userName') }}:</span>
                <span class="value">{{ order.userName }}</span>
              </div>
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.phone') }}:</span>
                <span class="value">{{ order.tel }}</span>
              </div>
              <div class="result-item">
                <span class="label">{{ $t('couponPropertyUser.purpose') }}:</span>
                <span class="value">{{ order.toTypeName }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('couponPropertyUser.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input v-model="conditions.couponName" :placeholder="$t('couponPropertyUser.couponNamePlaceholder')" />
            </el-col>
            <el-col :span="8">
              <el-input v-model="conditions.userName" :placeholder="$t('couponPropertyUser.userNamePlaceholder')" />
            </el-col>
            <el-col :span="6">
              <el-input v-model="conditions.tel" :placeholder="$t('couponPropertyUser.phonePlaceholder')" />
            </el-col>
            <el-col :span="2">
              <el-button type="primary" @click="handleQuery">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('couponPropertyUser.usageRecord') }}</span>
          </div>
          <el-table :data="couponPropertyUsers" border style="width: 100%" v-loading="loading">
            <el-table-column prop="uoId" :label="$t('couponPropertyUser.serialNumber')" align="center" />
            <el-table-column prop="couponName" :label="$t('couponPropertyUser.couponName')" align="center" />
            <el-table-column prop="value" :label="$t('couponPropertyUser.faceValue')" align="center" />
            <el-table-column prop="userName" :label="$t('couponPropertyUser.userName')" align="center" />
            <el-table-column prop="tel" :label="$t('couponPropertyUser.phone')" align="center" />
            <el-table-column prop="remark" :label="$t('couponPropertyUser.purpose')" align="center" />
            <el-table-column prop="createTime" :label="$t('couponPropertyUser.usageTime')" align="center" />
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" class="pagination" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listCouponPropertyUserDetail, writeOffCouponPropertyUser } from '@/api/scm/couponPropertyUserDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CouponPropertyUserDetailList',
  data() {
    return {
      loading: false,
      couponId: '',
      couponPropertyUsers: [],
      order: {
        remark: '',
        userName: '',
        createTime: '',
        tel: '',
        couponName: '',
        value: '',
        toTypeName: ''
      },
      conditions: {
        couponId: '',
        couponName: '',
        validityDay: '',
        userName: '',
        tel: '',
        toType: '',
        state: '',
        communityId: getCommunityId()
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.conditions
        }
        const { data, total } = await listCouponPropertyUserDetail(params)
        this.couponPropertyUsers = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('couponPropertyUser.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.page.current = 1
      this.getList()
    },
    async handleConfirmCoupon() {
      if (!this.couponId) {
        this.$message.warning(this.$t('couponPropertyUser.scanTip'))
        return
      }

      try {
        const data = {
          couponQrcode: this.couponId,
          communityId: this.conditions.communityId,
          giftCount: 1
        }
        const res = await writeOffCouponPropertyUser(data)
        if (res.code !== 0) {
          this.$message.error(res.msg)
          return
        }

        this.couponId = ''
        this.$message.success(this.$t('common.operationSuccess'))
        this.getList()

        if (res.data && res.data.length > 0) {
          this.order = { ...res.data[0] }
          if (!this.order.remark) {
            this.order.remark = this.$t('couponPropertyUser.verifySuccess')
          }
        }
      } catch (error) {
        this.couponId = ''
        this.$message.error(error.message)
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.coupon-property-user-detail-container {
  padding: 20px;

  .verification-card {
    height: 100%;

    .verification-wrapper {
      padding: 10px;

      .verification-result {
        margin-top: 20px;

        .result-item {
          display: flex;
          justify-content: space-between;
          margin-bottom: 10px;
          font-size: 14px;

          .label {
            color: #606266;
          }

          .value {
            color: #303133;
            font-weight: 500;
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .pagination {
      margin-top: 20px;
      text-align: right;
    }
  }

  .el-input {
    width: 100%;
  }
}
</style>