<template>
  <div class="gold-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div class="flex justify-between">
          <span>{{ $t('gold.title') }}</span>
          <div></div>
        </div>
      </div>

      <!-- 业主信息 -->
      <div class="">
        <el-row>
          <el-col :span="6">
            <div class="form-group">
              <label class="el-form-item__label">
                {{ $t('goldInfo.accountId') }}
              </label>
              <div class="el-form-item__content">
                {{ goldInfo.goldId }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="el-form-item__label">
                {{ $t('goldInfo.accountName') }}
              </label>
              <div class="el-form-item__content">
                {{ goldInfo.goldName }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="el-form-item__label">
                {{ $t('goldInfo.accountBalance') }}
              </label>
              <div class="el-form-item__content">
                {{ goldInfo.amount }}
                <el-button type="text" @click="openApplyWithholdGold">
                  {{ $t('gold.withdraw') }}
                </el-button>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <label class="el-form-item__label">
                {{ $t('goldInfo.communityName') }}
              </label>
              <div class="el-form-item__content">
                {{ goldInfo.communityName }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <divider></divider>

      <div class="margin-top-sm">
        <el-tabs v-model="goldInfo.currentTab" @tab-click="changeTab(goldInfo.currentTab)">
          <el-tab-pane :label="$t('gold.transactionDetails')" name="goldDetail">
            <gold-detail v-if="goldInfo.currentTab === 'goldDetail'" ref="goldDetail" />
          </el-tab-pane>
          <el-tab-pane :label="$t('gold.goldWithdrawal')" name="applyWithholdGold">
            <apply-withhold-gold v-if="goldInfo.currentTab === 'applyWithholdGold'" ref="applyWithholdGold" />
          </el-tab-pane>
        </el-tabs>
      </div>

      <withhold-gold ref="withholdGold" @success="handleSuccess" />
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getPropertyGoldInfo } from '@/api/scm/goldApi'
import GoldDetail from '@/components/scm/goldDetail'
import ApplyWithholdGold from '@/components/scm/applyWithholdGold'
import WithholdGold from '@/components/scm/withholdGold'
import divider from '@/components/system/divider' 

export default {
  name: 'GoldList',
  components: {
    GoldDetail,
    ApplyWithholdGold,
    WithholdGold,
    divider
  },
  data() {
    return {
      goldInfo: {
        goldId: '',
        goldName: '',
        amount: '',
        communityName: '',
        communityId: '',
        currentTab: 'goldDetail'
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadGoldInfo()
  },
  methods: {
    async loadGoldInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId
        }
        const { data } = await getPropertyGoldInfo(params)
        if (data && data.length > 0) {
          this.goldInfo = {
            ...this.goldInfo,
            goldId: data[0].goldId,
            goldName: data[0].goldName,
            amount: data[0].amount,
            communityName: data[0].communityName,
            communityId: data[0].communityId
          }
        }
      } catch (error) {
        console.error('Failed to load gold info:', error)
      }
    },
    changeTab(tab) {
      this.goldInfo.currentTab = tab.name || tab
      setTimeout(() => {
        this.$refs[tab].open(this.goldInfo)
      }, 500);
    },
    openApplyWithholdGold() {
      this.$refs.withholdGold.open({
        goldId: this.goldInfo.goldId,
        amount: this.goldInfo.amount,
        communityId: this.goldInfo.communityId
      })
    },
    handleSuccess() {
      this.loadGoldInfo()
      this.goldInfo.currentTab = 'applyWithholdGold'
    }
  }
}
</script>

<style lang="scss" scoped>
.gold-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }

  .form-group {
    text-align: left;
    margin-bottom: 0;
    padding: 10px;
    border-radius: 4px;

    .el-form-item__label {
      color: #606266;
    }

    .el-form-item__content {
    }
  }
}
</style>