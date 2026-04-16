<template>
  <el-dialog 
    :title="$t('inspectionItemTitleManage.addTitle')" 
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item 
        :label="$t('inspectionItemTitleManage.title')"
        prop="itemTitle"
        :rules="[{ required: true, message: $t('common.required') }]"
      >
        <el-input 
          v-model="formData.itemTitle" 
          :placeholder="$t('inspectionItemTitleManage.placeholderItemTitleRequired')"
        />
      </el-form-item>
      
      <el-form-item 
        :label="$t('inspectionItemTitleManage.titleType')"
        prop="titleType"
        :rules="[{ required: true, message: $t('common.required') }]"
      >
        <el-select 
          v-model="formData.titleType" 
          :placeholder="$t('inspectionItemTitleManage.placeholderTitleTypeRequired')"
          style="width:100%"
          @change="handleTypeChange"
        >
          <el-option 
            :label="$t('inspectionItemTitleManage.singleChoice')" 
            value="1001" 
          />
          <el-option 
            :label="$t('inspectionItemTitleManage.multipleChoice')" 
            value="2002" 
          />
          <el-option 
            :label="$t('inspectionItemTitleManage.shortAnswer')" 
            value="3003" 
          />
        </el-select>
      </el-form-item>
      
      <template v-if="formData.titleType && formData.titleType !== '3003'">
        <div v-for="(item, index) in formData.titleValues" :key="index">
          <el-form-item 
            :label="`${$t('inspectionItemTitleManage.option')} ${index + 1}`"
            :prop="`titleValues.${index}.itemValue`"
            :rules="[{ required: true, message: $t('common.required') }]"
          >
            <el-input v-model="item.itemValue" />
            <el-button 
              v-if="index === formData.titleValues.length - 1" 
              type="text" 
              @click="addOption"
            >
              <i class="el-icon-plus"></i>
              {{ $t('inspectionItemTitleManage.addOption') }}
            </el-button>
            <el-button 
              v-else 
              type="text" 
              @click="removeOption(index)"
            >
              <i class="el-icon-minus"></i>
              {{ $t('inspectionItemTitleManage.removeOption') }}
            </el-button>
          </el-form-item>
        </div>
      </template>
      
      <el-form-item 
        :label="$t('inspectionItemTitleManage.seq')"
        prop="seq"
        :rules="[
          { required: true, message: $t('common.required') },
          { type: 'number', message: $t('inspectionItemTitleManage.seqMustNumber') }
        ]"
      >
        <el-input 
          v-model.number="formData.seq" 
          :placeholder="$t('inspectionItemTitleManage.placeholderSeq')"
          type="number"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveInspectionItemTitle } from '@/api/inspection/inspectionItemTitleManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddInspectionItemTitle',
  data() {
    return {
      visible: false,
      formData: {
        titleType: '',
        itemTitle: '',
        seq: '',
        itemId: '',
        titleValues: []
      }
    }
  },
  methods: {
    open(data) {
      this.formData = {
        ...this.formData,
        ...data
      }
      this.visible = true
    },
    handleTypeChange(val) {
      if (val === '1001') {
        this.formData.titleValues = [{ itemValue: '', seq: 1 }]
      } else if (val === '2002') {
        this.formData.titleValues = [
          { itemValue: '', seq: 1 },
          { itemValue: '', seq: 2 }
        ]
      } else if (val === '3003') {
        this.formData.titleValues = []
      }
    },
    addOption() {
      this.formData.titleValues.push({
        itemValue: '',
        seq: this.formData.titleValues.length + 1
      })
    },
    removeOption(index) {
      this.formData.titleValues.splice(index, 1)
      // 重新排序
      this.formData.titleValues.forEach((item, i) => {
        item.seq = i + 1
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        titleType: '',
        itemTitle: '',
        seq: '',
        itemId: '',
        titleValues: []
      }
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        try {
          const params = {
            ...this.formData,
            communityId: getCommunityId()
          }
          
          await saveInspectionItemTitle(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('common.saveError'))
        }
      })
    }
  }
}
</script>