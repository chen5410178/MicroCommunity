<template>
  <el-dialog
    :title="$t('marketGoods.edit')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('marketGoods.name')"
        prop="name"
      >
        <el-input
          v-model="form.name"
          :placeholder="$t('marketGoods.name')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('marketGoods.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="4"
          :placeholder="$t('marketGoods.remark')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMarketGoods } from '@/api/market/marketWayApi'

export default {
  name: 'EditMarketGoods',
  data() {
    return {
      visible: false,
      form: {
        goodsId: '',
        name: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('marketGoods.name') + this.$t('common.required'), trigger: 'blur' },
          { max: 64, message: this.$t('marketGoods.name') + this.$t('common.maxLength64'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('marketGoods.remark') + this.$t('common.required'), trigger: 'blur' },
          { max: 512, message: this.$t('marketGoods.remark') + this.$t('common.maxLength512'), trigger: 'blur' }
        ],
        goodsId: [
          { required: true, message: 'ID' + this.$t('common.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMarketGoods(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        goodsId: '',
        name: '',
        remark: ''
      }
    }
  }
}
</script>