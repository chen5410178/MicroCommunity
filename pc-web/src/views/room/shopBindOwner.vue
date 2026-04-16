<template>
  <div class="shop-bind-owner-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('shops.handOver') }}</div>
        <div class="header-tools">
          <el-button type="primary" size="small" icon="el-icon-close" @click="_goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-row>
        <el-col :span="24">
          <el-form label-width="120px">
            <el-form-item :label="$t('shops.shopNumber')">
              <el-input v-model.trim="shopBindOwnerInfo.shopNum" :placeholder="$t('shops.shopNumberPlaceholder')" disabled />
            </el-form-item>

            <el-form-item :label="$t('shops.owner')">
              <el-col :span="18">
                <el-input v-model.trim="shopBindOwnerInfo.ownerName" :placeholder="$t('shops.tenantNameRequired')" disabled />
              </el-col>
              <el-col :span="4" :offset="1">
                <el-button type="primary" size="small" icon="el-icon-plus" @click="_openChooseOwner">
                  {{ $t('shops.selectOwner') }}
                </el-button>
              </el-col>
            </el-form-item>

            <el-form-item :label="$t('shops.leaseType')">
              <el-select v-model="shopBindOwnerInfo.leaseType" :placeholder="$t('shops.leaseTypePlaceholder')" @change="handleLeaseTypeChange" class="addLeaseType">
                <el-option :label="$t('shops.rent')" value="rent"></el-option>
                <el-option :label="$t('shops.sell')" value="sell"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item :label="$t('shops.startTime')">
              <el-date-picker v-model="shopBindOwnerInfo.startTime" type="date"
                :placeholder="$t('shops.startTimePlaceholder')" value-format="yyyy-MM-dd" class="addStartTime" />
            </el-form-item>

            <el-form-item :label="$t('shops.endTime')" v-if="shopBindOwnerInfo.leaseType === 'rent'">
              <el-date-picker v-model="shopBindOwnerInfo.endTime" type="date"
                :placeholder="$t('shops.endTimePlaceholder')" value-format="yyyy-MM-dd" class="addEndTime" />
            </el-form-item>

            <el-form-item :label="$t('shops.remark')">
              <el-input type="textarea" v-model="shopBindOwnerInfo.remark" :placeholder="$t('shops.remarkPlaceholder')" rows="3" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-row>
      <el-col :span="24" class="text-right" style="margin-top: 20px;">
        <el-button type="primary" icon="el-icon-check" @click="saveShopBindOwnerInfo">
          {{ $t('common.submit') }}
        </el-button>
      </el-col>
    </el-row>

    <search-owner ref="searchOwnerRef" @chooseOwner="handleChooseOwner" />
  </div>
</template>

<script>
import { saveShopOwner } from '@/api/room/shopOwnerApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDateYYYYMMDD } from '@/utils/dateUtil'
import SearchOwner from '@/components/owner/SearchOwner'

export default {
  name: 'ShopBindOwner',
  components: {
    SearchOwner
  },
  data() {
    return {
      shopBindOwnerInfo: {
        shopId: '',
        shopNum: '',
        ownerId: '',
        ownerName: '',
        leaseType: 'rent',
        startTime: '',
        endTime: '',
        remark: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.shopBindOwnerInfo.startTime = getDateYYYYMMDD()
    this.initData()
  },
  methods: {
    initData() {
      const shopId = this.$route.query.shopId
      const shopNum = this.$route.query.shopNum
      if (shopId) {
        this.shopBindOwnerInfo.shopId = shopId
        this.shopBindOwnerInfo.shopNum = shopNum || ''
      }
    },
    handleChooseOwner(owner) {
      this.shopBindOwnerInfo.ownerName = owner.name
      this.shopBindOwnerInfo.ownerId = owner.ownerId
      this.shopBindOwnerInfo.link = owner.link
    },
    _openChooseOwner() {
      this.$refs.searchOwnerRef.open()
    },
    handleLeaseTypeChange() {
      if (this.shopBindOwnerInfo.leaseType === 'sell') {
        this.shopBindOwnerInfo.endTime = '2050-01-01'
      } else {
        this.shopBindOwnerInfo.endTime = ''
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    shopBindOwnerValidate() {
      if (!this.shopBindOwnerInfo.ownerId) {
        this.$message.error(this.$t('shops.tenantNameRequired'))
        return false
      }
      if (!this.shopBindOwnerInfo.shopId) {
        this.$message.error(this.$t('shops.shopIdRequired'))
        return false
      }
      if (!this.shopBindOwnerInfo.startTime) {
        this.$message.error(this.$t('shops.startTimeRequired'))
        return false
      }
      if (this.shopBindOwnerInfo.leaseType === 'rent' && !this.shopBindOwnerInfo.endTime) {
        this.$message.error(this.$t('shops.endTimeRequired'))
        return false
      }
      if (this.shopBindOwnerInfo.startTime && this.shopBindOwnerInfo.endTime) {
        const start = new Date(this.shopBindOwnerInfo.startTime)
        const end = new Date(this.shopBindOwnerInfo.endTime)
        if (start >= end) {
          this.$message.error(this.$t('shops.timeError'))
          return false
        }
      }
      return true
    },
    async saveShopBindOwnerInfo() {
      if (!this.shopBindOwnerValidate()) return

      try {
        const state = this.shopBindOwnerInfo.leaseType === 'rent' ? '2006' : '2007'
        const data = {
          shopId: this.shopBindOwnerInfo.shopId,
          ownerId: this.shopBindOwnerInfo.ownerId,
          ownerName: this.shopBindOwnerInfo.ownerName,
          link: this.shopBindOwnerInfo.link,
          state: state,
          startTime: this.shopBindOwnerInfo.startTime,
          endTime: this.shopBindOwnerInfo.endTime || '2050-01-01',
          remark: this.shopBindOwnerInfo.remark,
          communityId: this.communityId
        }

        const res = await saveShopOwner(data)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg || this.$t('common.submitFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('common.requestError'))
      }
    }
  }
}
</script>

<style scoped>
.shop-bind-owner-container {
  padding: 20px;
}

.text-right {
  text-align: right;
}

.addStartTime,
.addEndTime,
.addLeaseType {
  width: 100%;
}
</style>
