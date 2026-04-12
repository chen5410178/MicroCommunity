<template>
  <div class="app-container">
    <div class="resizable-layout">
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <shop-tree-div ref="shopTreeDiv" @selectShop="handleSelectShop"></shop-tree-div>
      </div>

      <div class="resize-handle" @mousedown="startResize" @mouseover="showResizeCursor" @mouseleave="hideResizeCursor">
        <div class="resize-line"></div>
      </div>

      <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ shopCreateFeeInfo.shopName }}</span>
            <div class="header-tools">
              <el-button size="small" @click="_toBatchCreateFee">
                <i class="el-icon-plus"></i>
                {{ $t('shopCreateFee.batchCreate') }}
              </el-button>
            </div>
          </div>

          <div class="filter-container">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input v-model="shopCreateFeeInfo.condition.shopNum"
                  :placeholder="$t('shopCreateFee.shopNumPlaceholder')" clearable
                  @blur="listShopInShopCreateFee"></el-input>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" size="small" @click="_queryShopCreateFeeMethod">
                  {{ $t('common.query') }}
                </el-button>
                <el-button size="small" @click="_resetShopCreateFeeMethod">
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>

          <el-tabs v-model="shopCreateFeeInfo._currentTab" @tab-click="changeTab(shopCreateFeeInfo._currentTab)">
            <el-tab-pane :label="$t('shopCreateFee.shopFee')" name="simplifyShopFee">
              <simplify-shop-fee v-if="shopCreateFeeInfo._currentTab === 'simplifyShopFee'" ref="simplifyShopFee"
                :key="'simplifyShopFee' + shopCreateFeeInfo.shopId"></simplify-shop-fee>
            </el-tab-pane>
            <el-tab-pane v-if="shopCreateFeeInfo.ownerId" :label="$t('shopCreateFee.paymentHistory')"
              name="simplifyShopHisFee">
              <simplify-shop-his-fee v-if="shopCreateFeeInfo._currentTab === 'simplifyShopHisFee'" ref="simplifyShopHisFee"
                :key="'simplifyShopHisFee' + shopCreateFeeInfo.shopId"></simplify-shop-his-fee>
            </el-tab-pane>
            <el-tab-pane :label="$t('shopCreateFee.prePayFee')" name="simplifyShopPrePayFee">
              <simplify-shop-pre-pay-fee v-if="shopCreateFeeInfo._currentTab === 'simplifyShopPrePayFee'" ref="simplifyShopPrePayFee"
                :key="'simplifyShopPrePayFee' + shopCreateFeeInfo.shopId"></simplify-shop-pre-pay-fee>
            </el-tab-pane>
            <el-tab-pane :label="$t('shopCreateFee.billingRules')" name="feeDetailFeeRule">
              <fee-detail-fee-rule v-if="shopCreateFeeInfo._currentTab === 'feeDetailFeeRule'" ref="feeDetailFeeRule"
                :key="'feeDetailFeeRule' + shopCreateFeeInfo.shopId"></fee-detail-fee-rule>
            </el-tab-pane>
            <el-tab-pane :label="$t('shopCreateFee.setFee')" name="feeDetailShopFeeRule">
              <fee-detail-shop-fee-rule v-if="shopCreateFeeInfo._currentTab === 'feeDetailShopFeeRule'"
                ref="feeDetailShopFeeRule" :key="'feeDetailShopFeeRule' + shopCreateFeeInfo.shopId"></fee-detail-shop-fee-rule>
            </el-tab-pane>
            <el-tab-pane v-if="shopCreateFeeInfo.ownerId" :label="$t('shopCreateFee.ownerShops')" name="simplifyShopOwnerShops">
              <simplify-shop-owner-shops v-if="shopCreateFeeInfo._currentTab === 'simplifyShopOwnerShops'"
                ref="simplifyShopOwnerShops" :key="'simplifyShopOwnerShops' + shopCreateFeeInfo.shopId"></simplify-shop-owner-shops>
            </el-tab-pane>
            <el-tab-pane v-if="shopCreateFeeInfo.ownerId" :label="$t('shopCreateFee.owner')" name="simplifyShopOwner">
              <simplify-shop-owner v-if="shopCreateFeeInfo._currentTab === 'simplifyShopOwner'" ref="simplifyShopOwner"
                :key="'simplifyShopOwner' + shopCreateFeeInfo.shopId"></simplify-shop-owner>
            </el-tab-pane>
            <el-tab-pane :label="$t('shopCreateFee.receiptReprint')" name="simplifyFeeReceipt">
              <simplify-fee-receipt v-if="shopCreateFeeInfo._currentTab === 'simplifyFeeReceipt'"
                ref="simplifyFeeReceipt" :key="'simplifyFeeReceipt' + shopCreateFeeInfo.shopId"></simplify-fee-receipt>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import ShopTreeDiv from '@/components/fee/shopTreeDiv'
import SimplifyShopFee from '@/components/fee/simplifyShopFee'
import SimplifyShopHisFee from '@/components/fee/simplifyShopHisFee'
import SimplifyShopPrePayFee from '@/components/fee/simplifyShopPrePayFee'
import SimplifyShopOwnerShops from '@/components/fee/simplifyShopOwnerShops'
import SimplifyShopOwner from '@/components/fee/simplifyShopOwner'
import FeeDetailFeeRule from '@/components/fee/feeDetailFeeRule'
import SimplifyFeeReceipt from '@/components/fee/simplifyFeeReceipt'
import FeeDetailShopFeeRule from '@/components/fee/feeDetailShopFeeRule'
import { getCommunityId } from '@/api/community/communityApi'
import { listShops } from '@/api/shop/shopCreateFeeApi'

export default {
  name: 'ShopCreateFeeList',
  components: {
    ShopTreeDiv,
    SimplifyShopFee,
    SimplifyShopHisFee,
    SimplifyShopPrePayFee,
    SimplifyShopOwnerShops,
    SimplifyShopOwner,
    FeeDetailFeeRule,
    SimplifyFeeReceipt,
    FeeDetailShopFeeRule
  },
  data() {
    return {
      shopCreateFeeInfo: {
        shopName: '',
        shopId: '',
        shopNum: '',
        ownerId: '',
        ownerName: '',
        builtUpArea: '',
        locationId: '',
        locationName: '',
        _currentTab: 'simplifyShopFee',
        condition: {
          shopNum: ''
        },
        timer: null
      },
      communityId: '',
      treeWidth: 250,
      minTreeWidth: 200,
      maxTreeWidth: 600,
      isResizing: false,
      startX: 0,
      startWidth: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.restoreTreeWidth()
  },
  mounted() {
    document.addEventListener('mousemove', this.onMouseMove)
    document.addEventListener('mouseup', this.onMouseUp)
  },
  beforeDestroy() {
    document.removeEventListener('mousemove', this.onMouseMove)
    document.removeEventListener('mouseup', this.onMouseUp)
  },
  methods: {
    handleSelectShop(param) {
      this.shopCreateFeeInfo.ownerName = ''
      this.shopCreateFeeInfo.ownerId = param.ownerId || ''
      this.shopCreateFeeInfo.shopId = param.shopId
      this.shopCreateFeeInfo.shopName = param.shopName
      this.shopCreateFeeInfo.shopNum = param.shopNum || ''
      this.shopCreateFeeInfo.builtUpArea = param.builtUpArea || ''
      this.shopCreateFeeInfo.locationId = param.locationId || ''
      this.shopCreateFeeInfo.locationName = param.locationName || ''

      this.shopCreateFeeInfo.condition.shopNum = ''
      this.changeTab('simplifyShopFee')
    },
    async listShopInShopCreateFee() {
      if (!this.shopCreateFeeInfo.condition.shopNum) {
        return
      }
      const param = {
        page: 1,
        row: 1,
        communityId: this.communityId,
        shopNum: this.shopCreateFeeInfo.condition.shopNum
      }

      try {
        const res = await listShops(param)
        const shops = res.data || []
        if (shops.length > 0) {
          const shop = shops[0]
          this.shopCreateFeeInfo.shopId = shop.shopId
          this.shopCreateFeeInfo.shopName = shop.shopNum
          this.shopCreateFeeInfo.shopNum = shop.shopNum
          this.shopCreateFeeInfo.ownerId = shop.ownerId || ''
          this.shopCreateFeeInfo.ownerName = shop.ownerName || ''
          this.shopCreateFeeInfo.builtUpArea = shop.builtUpArea || ''
          this.shopCreateFeeInfo.locationId = shop.locationId || ''
          this.shopCreateFeeInfo.locationName = shop.locationName || ''
          this.changeTab('simplifyShopFee')
        }
      } catch (error) {
        console.error('Failed to load shop:', error)
      }
    },
    _queryShopCreateFeeMethod() {
      this.listShopInShopCreateFee()
    },
    _resetShopCreateFeeMethod() {
      this.shopCreateFeeInfo.shopNum = ''
      this.shopCreateFeeInfo.ownerName = ''
      this.shopCreateFeeInfo.ownerId = ''
      this.shopCreateFeeInfo.condition.shopNum = ''
      this.shopCreateFeeInfo._currentTab = 'simplifyShopFee'
    },
    changeTab(tab) {
      this.shopCreateFeeInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].open({
            shopId: this.shopCreateFeeInfo.shopId,
            shopName: this.shopCreateFeeInfo.shopName,
            shopNum: this.shopCreateFeeInfo.shopNum,
            ownerId: this.shopCreateFeeInfo.ownerId,
            ownerName: this.shopCreateFeeInfo.ownerName,
            builtUpArea: this.shopCreateFeeInfo.builtUpArea,
            payerObjId: this.shopCreateFeeInfo.shopId,
            payerObjType: '4444'
          })
        }
      }, 500)
    },
    _toBatchCreateFee() {
      this.$router.push('/pages/fee/shopCreatePayFee')
    },
    restoreTreeWidth() {
      const savedWidth = localStorage.getItem('shopCreateFeeTreeWidth')
      if (savedWidth) {
        const width = parseInt(savedWidth)
        if (width >= this.minTreeWidth && width <= this.maxTreeWidth) {
          this.treeWidth = width
        }
      }
    },
    saveTreeWidth() {
      localStorage.setItem('shopCreateFeeTreeWidth', this.treeWidth.toString())
    },
    startResize(event) {
      event.preventDefault()
      this.isResizing = true
      this.startX = event.clientX
      this.startWidth = this.treeWidth
      
      document.body.style.cursor = 'col-resize'
      document.body.style.userSelect = 'none'
    },
    onMouseMove(event) {
      if (!this.isResizing) return
      
      const deltaX = event.clientX - this.startX
      const newWidth = this.startWidth + deltaX
      
      if (newWidth >= this.minTreeWidth && newWidth <= this.maxTreeWidth) {
        this.treeWidth = newWidth
      }
    },
    onMouseUp() {
      if (this.isResizing) {
        this.isResizing = false
        this.saveTreeWidth()
        
        document.body.style.cursor = ''
        document.body.style.userSelect = ''
      }
    },
    showResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = 'col-resize'
      }
    },
    hideResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = ''
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  height: calc(100vh - 120px);
  overflow: hidden;
}

.resizable-layout {
  display: flex;
  flex-direction: row;
  height: 100%;
}

.tree-panel {
  background: #fff;
  border-radius: 4px;
  padding: 10px;
  border: 1px solid #e4e7ed;
}

.content-panel {
  overflow-y: auto;
}

.resize-handle {
  width: 10px;
  cursor: col-resize;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-handle:hover .resize-line {
  background-color: #409eff;
}

.resize-line {
  width: 2px;
  height: 30px;
  background-color: #dcdfe6;
  border-radius: 1px;
  transition: background-color 0.2s;
}

.header-tools {
  float: right;
}

.filter-container {
  margin-bottom: 20px;
}

.tree-panel * {
  user-select: none;
}

.resizable-layout.resizing * {
  user-select: none !important;
}
</style>
