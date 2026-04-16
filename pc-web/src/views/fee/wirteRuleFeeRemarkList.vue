<template>
  <div class="wirteRuleFeeRemark-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">{{ $t('wirteRuleFeeRemark.title') }}</span>
        <div class="card-tools">
          <el-button class="float-right" type="primary" @click="vc.goBack()">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

          <div class="">
            <el-form :model="wirteRuleFeeRemarkInfo" label-width="120px">
              <el-form-item :label="$t('wirteRuleFeeRemark.remark')">
                <el-input
                  v-model="wirteRuleFeeRemarkInfo.feeRemark"
                  type="textarea"
                  :rows="4"
                  :placeholder="$t('wirteRuleFeeRemark.remarkPlaceholder')"
                  class="form-control"
                />
              </el-form-item>
            </el-form>

            <div class="action-buttons">
              <el-button type="primary" @click="doModifyFeeRemark()">
                {{ $t('common.save') }}
              </el-button>
            </div>
          </div>

    </el-card>
  </div>
</template>

<script>
import { modifyRuleFeeRemark } from '@/api/fee/wirteRuleFeeRemarkApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WirteRuleFeeRemarkList',
  data() {
    return {
      wirteRuleFeeRemarkInfo: {
        ruleId: '',
        feeRemark: '',
        communityId: ''
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      this.wirteRuleFeeRemarkInfo.ruleId = this.$route.query.ruleId
      this.wirteRuleFeeRemarkInfo.communityId = getCommunityId()
    },
    async doModifyFeeRemark() {
      if (!this.wirteRuleFeeRemarkInfo.feeRemark) {
        this.$message.error(this.$t('wirteRuleFeeRemark.remarkRequired'))
        return
      }

      try {
        const result = await modifyRuleFeeRemark(this.wirteRuleFeeRemarkInfo)
        if (result.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(result.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.saveError'))
        console.error('保存失败:', error)
      }
    },
    vc: {
      goBack() {
        this.$router.go(-1)
      },
      i18n(key, namespace) {
        if (namespace) {
          return this.$t(`${namespace}.${key}`)
        }
        return this.$t(key)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.wirteRuleFeeRemark-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-title {
        font-size: 18px;
        font-weight: bold;
      }

      .card-tools {
        .float-right {
          float: right;
        }
      }
    }
  }

  .ibox-content {
    padding: 20px;

    .form-control {
      width: 100%;
    }

    .action-buttons {
      text-align: right;
      margin-top: 20px;

      .el-button {
        margin-left: 10px;
      }
    }
  }

  .el-form-item {
    margin-bottom: 20px;
  }
}
</style>