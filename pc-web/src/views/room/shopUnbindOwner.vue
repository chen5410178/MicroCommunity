<template>
  <div class="shop-unbind-owner-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('shops.checkOut') }}</div>
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
              <el-input v-model.trim="shopInfo.shopNum" disabled />
            </el-form-item>

            <el-form-item :label="$t('shops.owner')">
              <el-input v-model.trim="shopInfo.ownerName" disabled />
            </el-form-item>

            <el-form-item :label="$t('shops.contactNumber')">
              <el-input v-model.trim="shopInfo.link" disabled />
            </el-form-item>

            <el-form-item :label="$t('shops.state')">
              <el-tag v-if="shopInfo.state === '2006'" type="warning" size="mini">{{ $t('shops.rented') }}</el-tag>
              <el-tag v-else-if="shopInfo.state === '2007'" type="info" size="mini">{{ $t('shops.sold') }}</el-tag>
            </el-form-item>

            <el-form-item :label="$t('shops.startTime')">
              <el-input v-model.trim="shopInfo.startTime" disabled />
            </el-form-item>

            <el-form-item :label="$t('shops.endTime')">
              <el-input v-model.trim="shopInfo.endTime" disabled />
            </el-form-item>

            <el-form-item :label="$t('shops.remark')">
              <el-input type="textarea" v-model="shopInfo.remark" :placeholder="$t('shops.checkOutRemarkPlaceholder')" rows="3" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-row>
      <el-col :span="24" class="text-right" style="margin-top: 20px;">
        <el-button type="danger" icon="el-icon-check" @click="handleCheckOut">
          {{ $t('shops.confirmCheckOut') }}
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { deleteShopOwner, getShopOwnerInfo } from '@/api/room/shopOwnerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ShopUnbindOwner',
  data() {
    return {
      shopInfo: {
        shopId: '',
        shopNum: '',
        ownerId: '',
        ownerName: '',
        link: '',
        state: '',
        startTime: '',
        endTime: '',
        remark: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    initData() {
      const shopId = this.$route.query.shopId
      const shopNum = this.$route.query.shopNum
      const ownerId = this.$route.query.ownerId
      const ownerName = this.$route.query.ownerName
      const link = this.$route.query.link
      const state = this.$route.query.state

      this.shopInfo.shopId = shopId || ''
      this.shopInfo.shopNum = shopNum || ''
      this.shopInfo.ownerId = ownerId || ''
      this.shopInfo.ownerName = ownerName || ''
      this.shopInfo.link = link || ''
      this.shopInfo.state = state || ''

      if (shopId) {
        this.loadShopOwnerInfo(shopId)
      }
    },
    async loadShopOwnerInfo(shopId) {
      try {
        const res = await getShopOwnerInfo(shopId)
        if (res.data && res.data.length > 0) {
          const ownerInfo = res.data[0]
          this.shopInfo.ownerId = ownerInfo.ownerId || this.shopInfo.ownerId
          this.shopInfo.startTime = ownerInfo.startTime || ''
          this.shopInfo.endTime = ownerInfo.endTime || ''
        }
      } catch (error) {
        console.error('获取商铺业主信息失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    async handleCheckOut() {
      if (!this.shopInfo.shopId || !this.shopInfo.ownerId) {
        this.$message.error(this.$t('shops.checkOutError'))
        return
      }

      try {
        const data = {
          shopId: this.shopInfo.shopId,
          ownerId: this.shopInfo.ownerId,
          communityId: this.communityId,
          remark: this.shopInfo.remark
        }

        const res = await deleteShopOwner(data)
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
.shop-unbind-owner-container {
  padding: 20px;
}

.text-right {
  text-align: right;
}
</style>
