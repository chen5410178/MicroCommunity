<template>
  <el-dialog :title="$t('contractManage.edit.title')" :visible.sync="visible" width="70%" :before-close="handleClose">
    <el-form :model="editContractInfo" label-width="120px" class="text-left" ref="editForm">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.contractName')" prop="contractName" required>
            <el-input v-model="editContractInfo.contractName"
              :placeholder="$t('contractManage.edit.contractNamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.contractCode')" prop="contractCode" required>
            <el-input v-model="editContractInfo.contractCode"
              :placeholder="$t('contractManage.edit.contractCodePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.contractType')" prop="contractType" required>
            <el-select v-model="editContractInfo.contractType"
              :placeholder="$t('contractManage.edit.contractTypePlaceholder')" style="width:100%" disabled>
              <el-option v-for="item in editContractInfo.contractTypes" :key="item.contractTypeId"
                :label="item.typeName" :value="item.contractTypeId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.partyA')" prop="partyA" required>
            <el-input v-model="editContractInfo.partyA" :placeholder="$t('contractManage.edit.partyAPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.aContacts')" prop="aContacts" required>
            <el-input v-model="editContractInfo.aContacts"
              :placeholder="$t('contractManage.edit.aContactsPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.aLink')" prop="aLink" required>
            <el-input v-model="editContractInfo.aLink" :placeholder="$t('contractManage.edit.aLinkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.partyB')" prop="partyB" required>
            <el-input v-model="editContractInfo.partyB" :placeholder="$t('contractManage.edit.partyBPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.bContacts')" prop="bContacts" required>
            <el-input v-model="editContractInfo.bContacts"
              :placeholder="$t('contractManage.edit.bContactsPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.bLink')" prop="bLink" required>
            <el-input v-model="editContractInfo.bLink" :placeholder="$t('contractManage.edit.bLinkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.operator')" prop="operator" required>
            <el-input v-model="editContractInfo.operator"
              :placeholder="$t('contractManage.edit.operatorPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.operatorLink')" prop="operatorLink" required>
            <el-input v-model="editContractInfo.operatorLink"
              :placeholder="$t('contractManage.edit.operatorLinkPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.amount')" prop="amount">
            <el-input v-model="editContractInfo.amount" :placeholder="$t('contractManage.edit.amountPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.startTime')" prop="startTime" required>
            <el-date-picker v-model="editContractInfo.startTime" type="datetime"
              :placeholder="$t('contractManage.edit.startTimePlaceholder')" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.endTime')" prop="endTime" required>
            <el-date-picker v-model="editContractInfo.endTime" type="datetime"
              :placeholder="$t('contractManage.edit.endTimePlaceholder')" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="$t('contractManage.edit.signingTime')" prop="signingTime" required>
            <el-date-picker v-model="editContractInfo.signingTime" type="datetime"
              :placeholder="$t('contractManage.edit.signingTimePlaceholder')" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 合同类型规格 -->
      <div v-for="(item, index) in editContractInfo.contractTypeSpecs" :key="index">
        <el-row :gutter="20" v-if="index % 3 === 0">
          <el-col :span="8" v-for="i in 3" :key="i">
            <div v-if="editContractInfo.contractTypeSpecs[index + i - 1]">
              <el-form-item :label="editContractInfo.contractTypeSpecs[index + i - 1].specName">
                <el-input v-model="editContractInfo.contractTypeSpecs[index + i - 1].value"
                  :placeholder="editContractInfo.contractTypeSpecs[index + i - 1].specHoldplace" />
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 合同附件 -->
      <el-form-item :label="$t('contractManage.edit.contractFiles')">
        <el-button type="primary" @click="addFileStep">
          {{ $t('contractManage.edit.addFile') }}
        </el-button>
        <div v-for="(item, index) in editContractInfo.contractFilePo" :key="index" style="margin-top:10px">
          <el-row :gutter="10">
            <el-col :span="2">
              <span>{{ $t('contractManage.edit.fileIndex', { index: index + 1 }) }}</span>
            </el-col>
            <el-col :span="10">
              <input type="file" style="display:inline-block;width:100%" @change="getFile($event, index)"
                accept=".png,.pdf,.jpg" />
            </el-col>
            <el-col :span="6">
              <span>{{ item.fileRealName }}</span>
            </el-col>
            <el-col :span="4">
              <el-button type="text" @click="deleteStep(item)">
                {{ $t('contractManage.edit.deleteFile') }}
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('contractManage.edit.cancel') }}</el-button>
      <el-button type="primary" @click="editContract">{{ $t('contractManage.edit.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateContract, queryContractTypeSpec, queryContractFile } from '@/api/contract/contractManageApi'
import { uploadFile } from '@/api/system/paymentPoolApi'

export default {
  name: 'EditContract',
  data() {
    return {
      visible: false,
      editContractInfo: {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractType: '',
        partyA: '',
        partyB: '',
        aContacts: '',
        bContacts: '',
        aLink: '',
        bLink: '',
        operator: '',
        operatorLink: '',
        amount: '',
        startTime: '',
        endTime: '',
        signingTime: '',
        contractTypes: [],
        contractTypeSpecs: [],
        tempfile: null,
        contractFilePo: []
      }
    }
  },
  methods: {
    open(contract) {
      this.resetForm()
      this.editContractInfo = {
        ...this.editContractInfo,
        ...contract,
        contractTypes: this.editContractInfo.contractTypes
      }
      this.loadContractType(contract)
      this.loadFiles()
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    resetForm() {
      this.editContractInfo = {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractType: '',
        partyA: '',
        partyB: '',
        aContacts: '',
        bContacts: '',
        aLink: '',
        bLink: '',
        operator: '',
        operatorLink: '',
        amount: '',
        startTime: '',
        endTime: '',
        signingTime: '',
        contractTypes: this.editContractInfo.contractTypes,
        contractTypeSpecs: [],
        tempfile: null,
        contractFilePo: []
      }
    },
    async loadContractType(contract) {
      try {
        const params = {
          page: 1,
          row: 100,
          contractTypeId: contract.contractType
        }
        const { data } = await queryContractTypeSpec(params)
        this.editContractInfo.contractTypeSpecs = data.map(item => {
          const attr = contract.attrs.find(attr => attr.specCd === item.specCd) || {}
          return {
            ...item,
            value: attr.value || ''
          }
        }).filter(item => item.specShow === 'Y')
      } catch (error) {
        console.error('加载合同类型规格失败:', error)
      }
    },
    async loadFiles() {
      try {
        const params = {
          contractId: this.editContractInfo.contractId,
          page: 1,
          row: 100
        }
        const { data } = await queryContractFile(params)
        this.editContractInfo.contractFilePo = data.map((file, index) => ({
          seq: index,
          fileSaveName: file.fileSaveName,
          fileRealName: file.fileRealName
        }))
      } catch (error) {
        console.error('加载合同文件失败:', error)
      }
    },
    addFileStep() {
      const newFile = {
        seq: this.editContractInfo.contractFilePo.length,
        fileSaveName: '',
        fileRealName: ''
      }
      this.editContractInfo.contractFilePo.push(newFile)
    },
    deleteStep(step) {
      this.editContractInfo.contractFilePo = this.editContractInfo.contractFilePo.filter(
        item => item.seq !== step.seq
      )
    },
    getFile(event, index) {
      const file = event.target.files[0]
      if (!file) return

      if (!this.checkFileType(file.name.split('.').pop())) {
        this.$message.error(this.$t('contractManage.edit.fileTypeError'))
        return
      }

      this.editContractInfo.tempfile = file
      this.editContractInfo.contractFilePo[index].fileRealName = file.name
      this.uploadFile(index)
    },
    checkFileType(fileType) {
      const acceptTypes = ['png', 'pdf', 'jpg']
      return acceptTypes.includes(fileType.toLowerCase())
    },
    async uploadFile(index) {
      try {
        const formData = new FormData()
        formData.append('uploadFile', this.editContractInfo.tempfile)

        const data = await uploadFile(formData,{})
        this.editContractInfo.contractFilePo[index].fileSaveName = data.realFileName
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error('上传文件失败:', error)
        this.$message.error(this.$t('contractManage.edit.uploadError'))
      }
    },
    async editContract() {
      try {
        await updateContract(this.editContractInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('保存合同失败:', error)
        this.$message.error(error.message || this.$t('contractManage.edit.saveError'))
      }
    }
  }
}
</script>