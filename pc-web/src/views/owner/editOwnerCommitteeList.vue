<template>
  <div class="edit-owner-committee-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('editOwnerCommittee.title') }}</span>
        <div class="card-tools">
          <el-button type="primary" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>
      <el-form ref="form" :model="formData" label-width="120px" label-position="right" class="form-container">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.name')" prop="name"
              :rules="[{ required: true, message: $t('editOwnerCommittee.nameRequired'), trigger: 'blur' }]">
              <el-input v-model="formData.name" :placeholder="$t('editOwnerCommittee.namePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.sex')" prop="sex"
              :rules="[{ required: true, message: $t('editOwnerCommittee.sexRequired'), trigger: 'change' }]">
              <el-select v-model="formData.sex" :placeholder="$t('editOwnerCommittee.sexPlaceholder')" style="width:100%">
                <el-option v-for="item in sexOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.phone')" prop="link" :rules="[
              { required: true, message: $t('editOwnerCommittee.phoneRequired'), trigger: 'blur' },
              { min: 11, max: 11, message: $t('editOwnerCommittee.phoneLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.link" :placeholder="$t('editOwnerCommittee.phonePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.idCard')" prop="idCard" :rules="[
              { required: true, message: $t('editOwnerCommittee.idCardRequired'), trigger: 'blur' },
              { min: 18, max: 18, message: $t('editOwnerCommittee.idCardLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.idCard" :placeholder="$t('editOwnerCommittee.idCardPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.address')" prop="address" :rules="[
              { required: true, message: $t('editOwnerCommittee.addressRequired'), trigger: 'blur' },
              { max: 256, message: $t('editOwnerCommittee.addressMaxLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.address" :placeholder="$t('editOwnerCommittee.addressPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.position')" prop="position" :rules="[
              { required: true, message: $t('editOwnerCommittee.positionRequired'), trigger: 'blur' },
              { max: 64, message: $t('editOwnerCommittee.positionMaxLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.position" :placeholder="$t('editOwnerCommittee.positionPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.post')" prop="post" :rules="[
              { required: true, message: $t('editOwnerCommittee.postRequired'), trigger: 'blur' },
              { max: 64, message: $t('editOwnerCommittee.postMaxLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.post" :placeholder="$t('editOwnerCommittee.postPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.postDesc')" prop="postDesc"
              :rules="[{ max: 64, message: $t('editOwnerCommittee.postDescMaxLength'), trigger: 'blur' }]">
              <el-input v-model="formData.postDesc" :placeholder="$t('editOwnerCommittee.postDescPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.appointTime')" prop="appointTime" :rules="[
              { required: true, message: $t('editOwnerCommittee.appointTimeRequired'), trigger: 'blur' },
              { max: 256, message: $t('editOwnerCommittee.appointTimeMaxLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.appointTime" :placeholder="$t('editOwnerCommittee.appointTimePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.curTime')" prop="curTime" :rules="[
              { required: true, message: $t('editOwnerCommittee.curTimeRequired'), trigger: 'blur' },
              { max: 256, message: $t('editOwnerCommittee.curTimeMaxLength'), trigger: 'blur' }
            ]">
              <el-input v-model="formData.curTime" :placeholder="$t('editOwnerCommittee.curTimePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.status')" prop="state"
              :rules="[{ required: true, message: $t('editOwnerCommittee.statusRequired'), trigger: 'change' }]">
              <el-select v-model="formData.state" :placeholder="$t('editOwnerCommittee.statusPlaceholder')"
                style="width:100%">
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('editOwnerCommittee.remark')" prop="remark"
              :rules="[{ max: 200, message: $t('editOwnerCommittee.remarkMaxLength'), trigger: 'blur' }]">
              <el-input v-model="formData.remark" type="textarea"
                :placeholder="$t('editOwnerCommittee.remarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="box-card margin-top-20">
      <div slot="header" class="clearfix">
        <span>{{ $t('editOwnerCommittee.emergencyContact') }}</span>
        <div class="card-tools">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="addContract">
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table :data="formData.contracts" border style="width: 100%" class="contact-table">
        <el-table-column :label="$t('editOwnerCommittee.relation')" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.relName" :placeholder="$t('editOwnerCommittee.relationPlaceholder')" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('editOwnerCommittee.contactName')" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" :placeholder="$t('editOwnerCommittee.contactNamePlaceholder')" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('editOwnerCommittee.contactPhone')" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.link" :placeholder="$t('editOwnerCommittee.contactPhonePlaceholder')" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('editOwnerCommittee.contactAddress')" align="center" width="250">
          <template slot-scope="scope">
            <el-input v-model="scope.row.address" :placeholder="$t('editOwnerCommittee.contactAddressPlaceholder')" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="100">
          <template slot-scope="scope">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteContract(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div class="action-buttons margin-top-20">
      <el-button type="primary" icon="el-icon-check" @click="submitForm">
        {{ $t('common.save') }}
      </el-button>
      <el-button type="warning" class="margin-left-10" @click="goBack">
        {{ $t('common.back') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import {
  getOwnerCommitteeDetail,
  updateOwnerCommittee,
  listOwnerCommitteeContract
} from '@/api/owner/editOwnerCommitteeApi'
//import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditOwnerCommittee',
  data() {
    return {
      formData: {
        ocId: '',
        communityId: '',
        name: '',
        sex: '',
        link: '',
        idCard: '',
        address: '',
        position: '',
        post: '',
        postDesc: '',
        appointTime: '',
        curTime: '',
        state: '',
        remark: '',
        contracts: []
      },
      sexOptions: [
        { value: 'B', label: this.$t('editOwnerCommittee.male') },
        { value: 'G', label: this.$t('editOwnerCommittee.female') }
      ],
      statusOptions: [
        { value: '1000', label: this.$t('editOwnerCommittee.onDuty') },
        { value: '2000', label: this.$t('editOwnerCommittee.leave') }
      ]
    }
  },
  created() {
    this.formData.ocId = this.$route.query.ocId
    this.formData.communityId = getCommunityId()
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        // 获取业主委员会详情
        const detailRes = await getOwnerCommitteeDetail({
          ocId: this.formData.ocId,
          communityId: this.formData.communityId
        })
        Object.assign(this.formData, detailRes.data)

        // 获取紧急联系人列表
        const contractRes = await listOwnerCommitteeContract({
          ocId: this.formData.ocId,
          communityId: this.formData.communityId
        })
        this.formData.contracts = contractRes.data.map(item => ({
          ...item,
          id: item.contractId
        }))
      } catch (error) {
        this.$message.error(this.$t('editOwnerCommittee.fetchError'))
      }
    },
    addContract() {
      this.formData.contracts.push({
        id: Date.now().toString(),
        relName: '',
        name: '',
        link: '',
        address: ''
      })
    },
    deleteContract(contract) {
      const index = this.formData.contracts.findIndex(item => item.id === contract.id)
      if (index !== -1) {
        this.formData.contracts.splice(index, 1)
      }
    },
    async submitForm() {
      try {
        await this.$refs.form.validate()
        await updateOwnerCommittee(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        if (error instanceof Error) {
          this.$message.error(error.message)
        }
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-owner-committee-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-tools {
      display: flex;
      align-items: center;
    }
  }

  .form-container {
    padding: 20px;
  }

  .contact-table {
    margin-top: 10px;
  }

  .action-buttons {
    display: flex;
    justify-content: flex-end;
    padding: 20px 0;
  }

  .margin-top-20 {
    margin-top: 20px;
  }

  .margin-left-10 {
    margin-left: 10px;
  }
}
</style>