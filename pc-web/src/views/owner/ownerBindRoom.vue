<template>
    <div class="room-bind-owner-container">
        <el-card class="box-card">
            <div slot="header" class="flex justify-between">
                <div>{{ $t('roomBindOwner.title') }}</div>
                <div class="header-tools">
                    <el-button type="primary" size="small" icon="el-icon-close" @click="_goBack">
                        {{ $t('common.back') }}
                    </el-button>
                </div>
            </div>

            <el-row>
                <el-col :span="24">
                    <el-form label-width="120px">
                        <el-form-item :label="$t('roomBindOwner.owner')">
                            <el-input v-model.trim="roomBindOwnerInfo.ownerName"
                                :placeholder="$t('roomBindOwner.ownerPlaceholder')" disabled />
                        </el-form-item>
                        <el-form-item :label="$t('roomBindOwner.room')">
                            <el-col :span="18">
                                <el-input v-model.trim="roomBindOwnerInfo.roomName"
                                    :placeholder="$t('roomBindOwner.roomPlaceholder')" disabled />
                            </el-col>
                            <el-col :span="4" :offset="1">
                                <el-button type="primary" size="small" icon="el-icon-plus" @click="_openChooseRoom">
                                    {{ $t('listOwner.selectRoom') }}
                                </el-button>
                            </el-col>
                        </el-form-item>



                        <el-form-item :label="$t('roomBindOwner.startTime')">
                            <el-date-picker v-model="roomBindOwnerInfo.startTime" type="date"
                                :placeholder="$t('roomBindOwner.startTimePlaceholder')" value-format="yyyy-MM-dd"
                                class="addStartTime" />
                        </el-form-item>

                        <el-form-item :label="$t('roomBindOwner.endTime')">
                            <el-date-picker v-model="roomBindOwnerInfo.endTime" type="date"
                                :placeholder="$t('roomBindOwner.endTimePlaceholder')" value-format="yyyy-MM-dd"
                                class="addEndTime" />
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
        </el-card>

        <el-row>
            <el-col :span="24" class="text-right" style="margin-top: 20px;">
                <el-button type="primary" icon="el-icon-check" @click="saveRoomBindOwnerInfo">
                    {{ $t('common.submit') }}
                </el-button>
            </el-col>
        </el-row>

        <search-room ref="searchRoomRef" :roomFlag="roomFlag" @chooseRoom="handleChooseRoom" />
    </div>
</template>

<script>
import { sellRoom } from '@/api/owner/roomBindOwnerApi'
import { getCommunityId } from '@/api/community/communityApi'
import searchRoom from '@/components/room/searchRoom'
import { getDateYYYYMMDD } from '@/utils/dateUtil'
import { queryOwners } from '@/api/owner/ownerApi'

export default {
    name: 'RoomBindOwnerList',
    components: {
        searchRoom
    },
    data() {
        return {
            roomFlag: '2',
            roomBindOwnerInfo: {
                roomId: '',
                roomName: '',
                ownerId: '',
                ownerName: '',
                state: '2001',
                startTime: '',
                endTime: '2099-01-01',
            },
            communityId: ''
        }
    },
    created() {
        this.communityId = getCommunityId()
        this.roomBindOwnerInfo.startTime = getDateYYYYMMDD()
        this.initData()
    },
    methods: {
        initData() {
            const ownerId = this.$route.query.ownerId
            if (ownerId) {
                this.roomBindOwnerInfo.ownerId = ownerId
                this.listOwner(ownerId)
            }
        },
        async listOwner(ownerId) {
            try {
                const params = {
                    page: 1,
                    row: 1,
                    memberId: ownerId,
                    communityId: this.communityId
                }
                const { data } = await queryOwners(params)
                this.roomBindOwnerInfo.ownerName = data[0].name
            } catch (error) {
                this.$message.error(this.$t('roomBindOwner.fetchRoomError'))
            }
        },
        handleChooseRoom(room) {
            this.roomBindOwnerInfo.roomName = room.roomName
            this.roomBindOwnerInfo.roomId = room.roomId
        },
        _openChooseRoom() {
            this.$refs.searchRoomRef.open()
        },
        _goBack() {
            this.$router.go(-1)
        },
        roomBindOwnerValidate() {
            const rules = {
                ownerId: { required: true, message: this.$t('roomBindOwner.ownerRequired') },
                roomId: { required: true, message: this.$t('roomBindOwner.roomRequired') },
                startTime: { required: true, message: this.$t('roomBindOwner.startTimeRequired') },
                endTime: { required: true, message: this.$t('roomBindOwner.endTimeRequired') }
            }

            const errors = []
            Object.keys(rules).forEach(key => {
                if (!this.roomBindOwnerInfo[key]) {
                    errors.push(rules[key].message)
                }
            })

            if (errors.length > 0) {
                this.$message.error(errors[0])
                return false
            }

            const start = new Date(this.roomBindOwnerInfo.startTime)
            const end = new Date(this.roomBindOwnerInfo.endTime)
            if (start >= end) {
                this.$message.error(this.$t('roomBindOwner.timeError'))
                return false
            }

            return true
        },
        async saveRoomBindOwnerInfo() {
            if (!this.roomBindOwnerValidate()) return

            try {
                const data = {
                    ...this.roomBindOwnerInfo,
                    communityId: this.communityId
                }

                const res = await sellRoom(data)
                if (res.code === 0) {
                    this.$message.success(this.$t('common.operationSuccess'))
                    this._goBack()
                } else {
                    this.$message.error(res.msg || this.$t('common.submitFailed'))
                }
            } catch (error) {
                this.$message.error(this.$t('common.requestError'))
            }
        }
    }
}
</script>

<style scoped>
.room-bind-owner-container {
    padding: 20px;
}

.header-tools {}

.text-right {
    text-align: right;
}

.addStartTime,
.addEndTime {
    width: 100%;
}
</style>