<template>
  <el-dialog 
    :title="$t('storehouseManage.buttons.edit')" 
    :visible.sync="visible"
    width="50%"
    @close="close"
  >
    <el-form :model="form" label-width="150px" :rules="rules" ref="form">
      <el-form-item :label="$t('storehouseManage.table.shName')" prop="shName">
        <el-input 
          v-model="form.shName" 
          :placeholder="$t('storehouseManage.placeholders.shName')"
        />
      </el-form-item>
      <el-form-item :label="$t('storehouseManage.table.isShow')" prop="isShow">
        <el-select v-model="form.isShow" style="width:100%">
          <el-option :label="$t('storehouseManage.options.yes')" value="true" />
          <el-option :label="$t('storehouseManage.options.no')" value="false" />
        </el-select>
      </el-form-item>


      <el-form-item :label="$t('storehouseManage.table.allowPurchase')" prop="allowPurchase">
        <el-select v-model="form.allowPurchase" style="width:100%">
          <el-option :label="$t('storehouseManage.options.yes')" value="ON" />
          <el-option :label="$t('storehouseManage.options.no')" value="OFF" />
        </el-select>
      </el-form-item>


      <el-form-item :label="$t('storehouseManage.table.allowUse')" prop="allowUse">
        <el-select v-model="form.allowUse" style="width:100%">
          <el-option :label="$t('storehouseManage.options.yes')" value="ON" />
          <el-option :label="$t('storehouseManage.options.no')" value="OFF" />
        </el-select>
      </el-form-item>


      <el-form-item :label="$t('storehouseManage.table.purchaseSwitch')" prop="purchaseSwitch">
        <el-col :span="11">
          <el-select v-model="form.purchaseSwitch" style="width:100%">
            <el-option label="审核" value="ON" />
            <el-option label="不审核" value="OFF" />
          </el-select>
        </el-col>
        <el-col :span="11" :offset="2" v-if="form.purchaseSwitch === 'ON'">
          <el-form-item prop="purchaseRafId">
            <el-select v-model="form.purchaseRafId" :placeholder="$t('storehouseManage.placeholders.selectFlow')"
              style="width:100%">
              <el-option v-for="item in flows.filter(f => f.flowType === '5005')" :key="item.rafId"
                :label="item.flowName" :value="item.rafId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-form-item>
      <!-- 其他字段类似AddStorehouse组件 -->
      <el-form-item :label="$t('storehouseManage.table.useSwitch')" prop="useSwitch">
        <el-col :span="11">
          <el-select v-model="form.useSwitch" style="width:100%">
            <el-option label="审核" value="ON" />
            <el-option label="不审核" value="OFF" />
          </el-select>
        </el-col>
        <el-col :span="11" :offset="2" v-if="form.useSwitch === 'ON'">
          <el-form-item prop="useRafId">
            <el-select v-model="form.useRafId" :placeholder="$t('storehouseManage.placeholders.selectFlow')"
              style="width:100%">
              <el-option v-for="item in flows.filter(f => f.flowType === '6006')" :key="item.rafId"
                :label="item.flowName" :value="item.rafId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item :label="$t('storehouseManage.table.allocationSwitch')" prop="allocationSwitch">
        <el-col :span="11">
          <el-select v-model="form.allocationSwitch" style="width:100%">
            <el-option label="审核" value="ON" />
            <el-option label="不审核" value="OFF" />
          </el-select>
        </el-col>
        <el-col :span="11" :offset="2" v-if="form.allocationSwitch === 'ON'">
          <el-form-item prop="allocationRafId">
            <el-select v-model="form.allocationRafId" :placeholder="$t('storehouseManage.placeholders.selectFlow')"
              style="width:100%">
              <el-option v-for="item in flows.filter(f => f.flowType === '7007')" :key="item.rafId"
                :label="item.flowName" :value="item.rafId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-form-item>
    </el-form>
    
    <div slot="footer">
      <el-button @click="close">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="editStorehouse">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateStorehouse, listResourceAuditFlow } from '@/api/resource/storehouseManageApi'

export default {
  name: 'EditStorehouse',
  data() {
    return {
      visible: false,
      form: {
        shId: '',
        shName: '',
        isShow: '',
        allowPurchase: 'ON',
        allowUse: 'ON',
        purchaseSwitch: '',
        purchaseRafId: '',
        useSwitch: '',
        useRafId: '',
        allocationSwitch: '',
        allocationRafId: '',
        shDesc: '',
        communityId: getCommunityId()
      },
      flows: [],
      rules: {
        shName: [
          { required: true, message: this.$t('storehouseManage.messages.shNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('storehouseManage.messages.shNameMaxLength'), trigger: 'blur' }
        ],
        isShow: [
          { required: true, message: this.$t('storehouseManage.messages.isShowRequired'), trigger: 'change' }
        ],
        shId: [
          { required: true, message: this.$t('storehouseManage.messages.shIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
      this.listResourceAuditFlows()
    },
    close() {
      this.visible = false
      this.$refs.form.resetFields()
    },
    async listResourceAuditFlows() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const response = await listResourceAuditFlow(params)
        this.flows = response.data
      } catch (error) {
        this.$message.error(this.$t('storehouseManage.messages.flowFetchError'))
      }
    },
    async editStorehouse() {
      try {
        await this.$refs.form.validate()
        await updateStorehouse(this.form)
        this.$emit('success')
        this.close()
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        if (error.message) {
          this.$message.error(error.message)
        }
      }
    }
  }
}
</script>