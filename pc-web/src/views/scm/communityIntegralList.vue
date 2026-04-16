<template>
  <div class="community-integral-container ">
    <el-card>
      <div class="white-bg border-radius">
        <div class="flex justify-between">
          <span>{{ $t('communityIntegral.title') }}</span>
        </div>

        <!-- 业主信息 -->
        <div class="margin-top">
          <el-row>
            <el-col :span="8">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('communityIntegral.accountId') }}
                </label>
                <label>{{ communityIntegralInfo.integralId }}</label>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('communityIntegral.accountName') }}
                </label>
                <label>{{ communityIntegralInfo.integralName }}</label>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="form-group">
                <label class="col-form-label">
                  {{ $t('communityIntegral.points') }}
                </label>
                <label>
                  {{ communityIntegralInfo.amount }}
                  <el-link type="primary" @click="openApplyWithholdIntegral">{{ $t('communityIntegral.withdraw')
                    }}</el-link>
                </label>
              </div>
            </el-col>
          </el-row>
        </div>

        <divider></divider>

        <div class="margin-top">
          <el-tabs v-model="communityIntegralInfo._currentTab"
            @tab-click="changeTab(communityIntegralInfo._currentTab)">
            <el-tab-pane :label="$t('communityIntegral.transactionDetails')" name="communityIntegralDetail">
              <community-integral-detail v-if="communityIntegralInfo._currentTab === 'communityIntegralDetail'"
                ref="communityIntegralDetail" />
            </el-tab-pane>
            <el-tab-pane :label="$t('communityIntegral.pointsWithdrawal')" name="applyWithholdIntegral">
              <apply-withhold-integral v-if="communityIntegralInfo._currentTab === 'applyWithholdIntegral'"
                ref="applyWithholdIntegral" />
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>

      <withhold-integral ref="withholdIntegral" @success="handleWithdrawSuccess" />
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryCommunityIntegral } from '@/api/scm/communityIntegralApi'
import CommunityIntegralDetail from '@/components/scm/communityIntegralDetail'
import ApplyWithholdIntegral from '@/components/scm/applyWithholdIntegral'
import WithholdIntegral from '@/components/scm/withholdIntegral'
import divider from '@/components/system/divider'

export default {
  name: 'CommunityIntegralList',
  components: {
    CommunityIntegralDetail,
    ApplyWithholdIntegral,
    WithholdIntegral,
    divider
  },
  data() {
    return {
      communityIntegralInfo: {
        integralId: "",
        integralName: '',
        amount: "",
        communityName: "",
        communityId: "",
        _currentTab: 'communityIntegralDetail'
      }
    }
  },
  created() {
    this.communityIntegralInfo.communityId = getCommunityId()
    this.loadCommunityIntegralInfo()
  },
  methods: {
    async loadCommunityIntegralInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityIntegralInfo.communityId
        }
        const { data } = await queryCommunityIntegral(params)
        if (data && data.length > 0) {
          this.communityIntegralInfo = {
            ...this.communityIntegralInfo,
            ...data[0]
          }
          this.changeTab(this.communityIntegralInfo._currentTab)
        }
      } catch (error) {
        console.error('Failed to load community integral info:', error)
      }
    },
    changeTab(tab) {
      this.communityIntegralInfo._currentTab = tab
      setTimeout(() => {
        this.$refs[tab].open({
          integralId: this.communityIntegralInfo.integralId,
          integralName: this.communityIntegralInfo.integralName,
          amount: this.communityIntegralInfo.amount,
          communityId: this.communityIntegralInfo.communityId
        })
      }, 500);
    },
    openApplyWithholdIntegral() {
      this.$refs.withholdIntegral.open({
        integralId: this.communityIntegralInfo.integralId,
        amount: this.communityIntegralInfo.amount,
        communityId: this.communityIntegralInfo.communityId
      })
    },
    handleWithdrawSuccess() {
      this.communityIntegralInfo._currentTab = 'applyWithholdIntegral'
      this.changeTab(this.communityIntegralInfo._currentTab)
      this.loadCommunityIntegralInfo()
    }
  }
}
</script>

<style scoped>
.community-integral-container {
  margin:20px
}

.white-bg {
  background-color: #fff;
}

.padding-lg {}

.padding-top {}

.border-radius {
  border-radius: 4px;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.form-group {
  margin-bottom: 20px;
}

.col-form-label {
  margin-right: 10px;
}
</style>