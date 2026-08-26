<template>
  <el-dialog :title="formTitle" :visible.sync="dialogVisible" width="50%">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('shops.shopNumber')">
        <el-input v-model="formData.shopsName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.tenantPhone')" prop="tel">
        <el-input v-model="formData.tel" :placeholder="$t('shops.tenantPhoneRequired')" @blur="loadOwnerInfo"></el-input>
      </el-form-item>
      <el-form-item :label="$t('shops.tenantName')" prop="ownerName">
        <el-input v-model="formData.ownerName" :placeholder="$t('shops.tenantNameRequired')"></el-input>
      </el-form-item>
      <el-form-item v-if="formData.shopsState === '2006'" :label="$t('shops.leaseStart')" prop="startTime">
        <el-date-picker v-model="formData.startTime" type="date" :placeholder="$t('shops.leaseStartRequired')"
          value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
      </el-form-item>
      <el-form-item v-if="formData.shopsState === '2006'" :label="$t('shops.leaseEnd')" prop="endTime">
        <el-date-picker v-model="formData.endTime" type="date" :placeholder="$t('shops.leaseEndRequired')"
          value-format="yyyy-MM-dd" :picker-options="endDateOptions" style="width: 100%"></el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('shops.remark')">
        <el-input type="textarea" v-model="formData.remark" :placeholder="$t('shops.remark')"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOwnerShops } from '@/api/room/shopsApi'
import {queryOwners} from '@/api/owner/ownerApi'
import {getCommunityId} from '@/api/community/communityApi'
import {getDateYYYYMMDD} from '@/utils/dateUtil'

export default {
  name: 'BindOwnerShops',
  data() {
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error(this.$t('shops.tenantPhoneRequired')))
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error(this.$t('shops.phoneFormatError')))
      } else {
        callback()
      }
    }

    const validateEndDate = (rule, value, callback) => {
      if (!value) {
        callback(new Error(this.$t('shops.leaseEndRequired')))
      } else if (new Date(value) <= new Date(this.formData.startTime)) {
        callback(new Error(this.$t('shops.leaseEndRequired')))
      } else {
        callback()
      }
    }

    return {
      dialogVisible: false,
      formData: {
        roomId: '',
        shopsName: '',
        ownerId: '',
        ownerName: '',
        tel: '',
        startTime: '',
        endTime: '',
        remark: '',
        shopsState: '2006'
      },
      rules: {
        tel: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        ownerName: [
          { required: true, message: this.$t('shops.tenantNameRequired'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('shops.leaseStartRequired'), trigger: 'change' }
        ],
        endTime: [
          { validator: validateEndDate, trigger: 'change' }
        ]
      },
      endDateOptions: {
        disabledDate: (time) => {
          if (this.formData.startTime) {
            return time.getTime() <= new Date(this.formData.startTime).getTime()
          }
          return false
        }
      }
    }
  },
  computed: {
    formTitle() {
      return this.formData.shopsState === '2007'
        ? this.$t('shops.sellShop') 
        : this.$t('shops.rentShop')
    }
  },
  methods: {
    open(row, state) {
      this.dialogVisible = true
      this.resetForm()
      this.formData = {
        roomId: row.roomId,
        shopsName: `${row.floorNum}-${row.roomNum}`,
        ownerId: '',
        ownerName: '',
        tel: '',
        startTime: state === '2007' ? getDateYYYYMMDD() : '',
        endTime: state === '2007' ? '2050-01-01' : '',
        remark: '',
        shopsState: state
      }
    },
    resetForm() {
      this.formData = {
        roomId: '',
        shopsName: '',
        ownerId: '',
        ownerName: '',
        tel: '',
        startTime: '',
        endTime: '',
        remark: '',
        shopsState: '2006'
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
   async loadOwnerInfo() {
      if (!this.formData.tel) return

     const {data,total}  = await queryOwners({page:1,row:1,communityId:getCommunityId(),link: this.formData.tel});
     if(total>0){
       this.formData.ownerName = data[0].name
       this.formData.ownerId = data[0].ownerId
     }else{
       this.formData.ownerName = ''
       this.formData.ownerId = ''
     }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.saveData()
        }
      })
    },
    saveData() {
      saveOwnerShops(this.formData)
        .then(res => {
          if (res.code == 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } else {
            this.$message.error(res.msg)
          }
        })
        .catch(error => {
          console.error(error)
          this.$message.error(this.$t('common.saveFailed'))
        })
    }
  }
}
</script>