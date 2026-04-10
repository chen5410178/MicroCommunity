<template>
  <el-dialog
    :title="$t('marketRule.associateMarketingMethod')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-form-item :label="$t('marketRule.marketingType')" prop="wayType">
        <el-select 
          v-model="form.wayType" 
          :placeholder="$t('marketRule.selectMarketingType')"
          @change="handleWayTypeChange"
        >
          <el-option
            v-for="item in wayTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item 
        v-if="form.wayType === '1001'"
        :label="$t('marketRule.marketingMethod')"
        prop="wayObjId"
      >
        <el-select 
          v-model="form.wayObjId" 
          :placeholder="$t('marketRule.selectMarketingText')"
        >
          <el-option
            v-for="item in marketTexts"
            :key="item.textId"
            :label="item.name"
            :value="item.textId"
          />
        </el-select>
      </el-form-item>

      <el-form-item 
        v-if="form.wayType === '2002'"
        :label="$t('marketRule.marketingMethod')"
        prop="wayObjId"
      >
        <el-select 
          v-model="form.wayObjId" 
          :placeholder="$t('marketRule.selectMarketingImage')"
        >
          <el-option
            v-for="item in marketPics"
            :key="item.picId"
            :label="item.name"
            :value="item.picId"
          />
        </el-select>
      </el-form-item>

      <el-form-item 
        v-if="form.wayType === '3003'"
        :label="$t('marketRule.marketingMethod')"
        prop="wayObjId"
      >
        <el-select 
          v-model="form.wayObjId" 
          :placeholder="$t('marketRule.selectMarketingGoods')"
        >
          <el-option
            v-for="item in marketGoodss"
            :key="item.goodsId"
            :label="item.name"
            :value="item.goodsId"
          />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('marketRule.remark')" prop="remark">
        <el-input
          type="textarea"
          v-model="form.remark"
          :placeholder="$t('marketRule.optional')"
          rows="4"
        ></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('marketRule.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('marketRule.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { 
  saveMarketRuleWay,
  listMarketText,
  listMarketPic,
  listMarketGoods 
} from '@/api/market/marketRuleApi'

export default {
  name: 'AddMarketRuleWay',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      form: {
        ruleId: '',
        wayType: '',
        wayObjId: '',
        remark: ''
      },
      wayTypes: [
        { value: '1001', label: this.$t('marketRule.text') },
        { value: '2002', label: this.$t('marketRule.image') },
        { value: '3003', label: this.$t('marketRule.goods') }
      ],
      marketTexts: [],
      marketPics: [],
      marketGoodss: [],
      rules: {
        wayType: [
          { required: true, message: this.$t('marketRule.required'), trigger: 'change' }
        ],
        wayObjId: [
          { required: true, message: this.$t('marketRule.required'), trigger: 'change' }
        ],
        remark: [
          { max: 512, message: this.$t('marketRule.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.form.ruleId = this.ruleId
      this.visible = true
      this.loadMarketTexts()
      this.loadMarketPics()
      this.loadMarketGoodss()
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        ruleId: this.ruleId,
        wayType: '',
        wayObjId: '',
        remark: ''
      }
    },
    handleWayTypeChange() {
      this.form.wayObjId = ''
    },
    async loadMarketTexts() {
      try {
        const { data } = await listMarketText({ page: 1, row: 100 })
        this.marketTexts = data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async loadMarketPics() {
      try {
        const { data } = await listMarketPic({ page: 1, row: 100 })
        this.marketPics = data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async loadMarketGoodss() {
      try {
        const { data } = await listMarketGoods({ page: 1, row: 100 })
        this.marketGoodss = data
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveMarketRuleWay(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>