<template>
  <el-dialog :title="$t('maintainanceItem.edit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('maintainanceItem.form.titleType')" prop="titleType">
        <el-select v-model="form.titleType" :placeholder="$t('maintainanceItem.form.titleTypePlaceholder')"
          style="width:100%" @change="handleTitleTypeChange">
          <el-option v-for="item in titleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('maintainanceItem.form.itemTitle')" prop="itemTitle">
        <el-input v-model="form.itemTitle" :placeholder="$t('maintainanceItem.form.itemTitlePlaceholder')" />
      </el-form-item>

      <template v-if="form.titleType && form.titleType !== '3003'">
        <el-form-item v-for="(item, index) in form.titleValues" :key="index"
          :label="`${$t('maintainanceItem.form.option')} ${index + 1}`" :prop="'titleValues.' + index + '.itemValue'"
          :rules="{
            required: true,
            message: $t('maintainanceItem.form.optionRequired'),
            trigger: 'blur'
          }">
          <el-row :gutter="10">
            <el-col :span="20">
              <el-input v-model="item.itemValue" :placeholder="$t('maintainanceItem.form.optionPlaceholder')" />
            </el-col>
            <el-col :span="3">
              <el-button v-if="index === form.titleValues.length - 1" type="text" @click="handleAddOption">
                {{ $t('common.add') }}
              </el-button>
              <el-button v-else type="text" @click="handleRemoveOption(index)">
                {{ $t('common.delete') }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </template>

      <el-form-item :label="$t('maintainanceItem.form.seq')" prop="seq">
        <el-input v-model.number="form.seq" :placeholder="$t('maintainanceItem.form.seqPlaceholder')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMaintainanceItem } from '@/api/inspection/maintainanceItemApi'

export default {
  name: 'EditMaintainanceItem',
  data() {
    return {
      visible: false,
      form: {
        titleId: '',
        titleType: '',
        itemTitle: '',
        seq: '',
        titleValues: [],
        communityId: ''
      },
      rules: {
        itemTitle: [
          { required: true, message: this.$t('maintainanceItem.form.itemTitleRequired'), trigger: 'blur' }
        ],
        titleType: [
          { required: true, message: this.$t('maintainanceItem.form.titleTypeRequired'), trigger: 'change' }
        ],
        seq: [
          { required: true, message: this.$t('maintainanceItem.form.seqRequired'), trigger: 'blur' },
        ]
      },
      titleTypeOptions: [
        { value: '1001', label: this.$t('maintainanceItem.titleType.single') },
        { value: '2002', label: this.$t('maintainanceItem.titleType.multiple') },
        { value: '3003', label: this.$t('maintainanceItem.titleType.shortAnswer') }
      ]
    }
  },
  methods: {
    open(data) {
      this.resetForm()
      if (data) {
        this.form = {
          ...this.form,
          titleId: data.titleId,
          itemId: data.itemId,
          titleType: data.titleType,
          itemTitle: data.itemTitle,
          seq: data.seq,
          titleValues: data.titleValues || [],
          communityId: data.communityId
        }
      }
      this.visible = true
    },
    resetForm() {
      this.form = {
        titleId: '',
        titleType: '',
        itemTitle: '',
        seq: '',
        titleValues: [],
        communityId: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.resetForm()
    },
    handleTitleTypeChange(value) {
      if (value === '3003') {
        this.form.titleValues = []
      } else if (value === '1001' && (!this.form.titleValues || this.form.titleValues.length === 0)) {
        this.form.titleValues = [{ itemValue: '', seq: 1 }]
      } else if (value === '2002' && (!this.form.titleValues || this.form.titleValues.length === 0)) {
        this.form.titleValues = [
          { itemValue: '', seq: 1 },
          { itemValue: '', seq: 2 }
        ]
      }
    },
    handleAddOption() {
      this.form.titleValues.push({
        itemValue: '',
        seq: this.form.titleValues.length + 1
      })
    },
    handleRemoveOption(index) {
      this.form.titleValues.splice(index, 1)
      // 重新排序
      this.form.titleValues.forEach((item, i) => {
        item.seq = i + 1
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMaintainanceItem(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('common.updateFailed'))
          }
        }
      })
    }
  }
}
</script>