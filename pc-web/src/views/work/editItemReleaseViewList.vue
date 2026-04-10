<template>
  <div class="edit-item-release-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editItemReleaseView.title') }}</span>
        <div>
          <el-button type="text" size="small" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-card shadow="never">
            <el-form label-position="right" label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.releaseType')">
                    <el-select v-model="editItemReleaseViewInfo.typeId" disabled style="width:100%">
                      <el-option disabled value="" :label="$t('editItemReleaseView.selectReleaseType')" />
                      <template v-for="item in editItemReleaseViewInfo.itemReleaseTypes">
                        <el-option :key="item.typeId" :label="item.typeName" :value="item.typeId"
                          v-if="item.state == 'C'" />
                      </template>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.applyCompany')">
                    <el-input v-model.trim="editItemReleaseViewInfo.applyCompany"
                      :placeholder="$t('editItemReleaseView.inputApplyCompany')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.applyPerson')">
                    <el-input v-model.trim="editItemReleaseViewInfo.applyPerson"
                      :placeholder="$t('editItemReleaseView.inputApplyPerson')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.idCard')">
                    <el-input v-model.trim="editItemReleaseViewInfo.idCard"
                      :placeholder="$t('editItemReleaseView.inputIdCard')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.phone')">
                    <el-input v-model.trim="editItemReleaseViewInfo.applyTel"
                      :placeholder="$t('editItemReleaseView.inputPhone')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.passTime')">
                    <el-date-picker v-model="editItemReleaseViewInfo.passTime" type="datetime"
                      :placeholder="$t('editItemReleaseView.selectPassTime')" style="width:100%" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.carNum')">
                    <el-input v-model.trim="editItemReleaseViewInfo.carNum"
                      :placeholder="$t('editItemReleaseView.inputCarNum')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('editItemReleaseView.remark')">
                    <el-input v-model.trim="editItemReleaseViewInfo.remark" type="textarea"
                      :placeholder="$t('editItemReleaseView.inputRemark')" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="margin-top">
        <el-col :span="24">
          <el-card shadow="never">
            <div slot="header" class="flex justify-between">
              <span>{{ $t('editItemReleaseView.releaseItems') }}</span>
              <el-button type="primary" size="small" style="float: right;" @click="addResName">
                <i class="el-icon-plus"></i>
                {{ $t('editItemReleaseView.addItem') }}
              </el-button>
            </div>

            <el-table :data="editItemReleaseViewInfo.resNames" border style="width: 100%">
              <el-table-column :label="$t('editItemReleaseView.itemName')" align="center">
                <template slot-scope="scope">
                  <el-input v-model.trim="scope.row.resName" :placeholder="$t('editItemReleaseView.inputItemName')" />
                </template>
              </el-table-column>
              <el-table-column :label="$t('editItemReleaseView.itemAmount')" align="center">
                <template slot-scope="scope">
                  <el-input v-model.trim="scope.row.amount" type="number"
                    :placeholder="$t('editItemReleaseView.inputItemAmount')" />
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.operation')" align="center" width="120">
                <template slot-scope="scope">
                  <el-button type="danger" size="mini" @click="removeResName(scope.row.resName)">
                    {{ $t('common.delete') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <div class="action-buttons">
        <el-button type="primary" @click="updateItemReleaseInfo">
          {{ $t('common.submit') }}
        </el-button>
        <el-button type="warning" @click="goBack">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  updateItemRelease,
  listItemReleaseType,
  listItemRelease,
  listItemReleaseRes
} from '@/api/work/editItemReleaseViewApi'

export default {
  name: 'EditItemReleaseView',
  data() {
    return {
      editItemReleaseViewInfo: {
        irId: '',
        typeId: '',
        applyCompany: '',
        applyPerson: '',
        idCard: '',
        applyTel: '',
        passTime: '',
        resNames: [],
        state: '',
        carNum: '',
        remark: '',
        itemReleaseTypes: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.editItemReleaseViewInfo.irId = this.$route.query.irId
    this.listItemReleaseTypes()
    this.listItemReleases()
    this.loadItemReleaseRes()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    validateForm() {
      // 这里添加表单验证逻辑
      return true
    },
    async updateItemReleaseInfo() {
      if (!this.validateForm()) {
        return
      }

      try {
        this.editItemReleaseViewInfo.communityId = this.communityId
        const res = await updateItemRelease(this.editItemReleaseViewInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.updateFailed'))
      }
    },
    async listItemReleaseTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const res = await listItemReleaseType(params)
        this.editItemReleaseViewInfo.itemReleaseTypes = res.data
      } catch (error) {
        console.error('Failed to fetch item release types:', error)
      }
    },
    addResName() {
      this.editItemReleaseViewInfo.resNames.push({
        resName: '',
        amount: ''
      })
    },
    removeResName(resName) {
      this.editItemReleaseViewInfo.resNames = this.editItemReleaseViewInfo.resNames.filter(
        item => item.resName !== resName
      )
    },
    async listItemReleases() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          irId: this.editItemReleaseViewInfo.irId
        }
        const res = await listItemRelease(params)
        if (res.data && res.data.length > 0) {
          Object.assign(this.editItemReleaseViewInfo, res.data[0])
        }
      } catch (error) {
        console.error('Failed to fetch item releases:', error)
      }
    },
    async loadItemReleaseRes() {
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.communityId,
          irId: this.editItemReleaseViewInfo.irId
        }
        const res = await listItemReleaseRes(params)
        this.editItemReleaseViewInfo.resNames = res.data
      } catch (error) {
        console.error('Failed to fetch item release resources:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-item-release-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .header-tools {
    float: right;
  }

  .margin-top {
    margin-top: 20px;
  }

  .action-buttons {
    margin-top: 20px;
    text-align: right;

    .el-button {
      margin-left: 10px;
    }
  }
}
</style>