-- V7: Create master_constant_type lookup table and convert string enums to int
-- =============================================================================

-- =============================================================================
-- MASTER CONSTANT TYPE (Lookup table for enum codes)
-- Extends BaseEntity pattern
-- =============================================================================
CREATE TABLE master_constant_type (
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  UUID,
    updated_by  UUID,
    status      INT NOT NULL DEFAULT 1,
    
    code        INT NOT NULL,
    category    VARCHAR(50) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    note        TEXT,
    color       VARCHAR(20),
    
    UNIQUE (category, code),
    UNIQUE (category, name)
);

-- =============================================================================
-- INSERT ENUM DATA
-- =============================================================================

-- EntityStatus
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'EntityStatus', 1, 'ACTIVE', 'Đang hoạt động'),
(gen_random_uuid(), 1, 'EntityStatus', 2, 'INACTIVE', 'Không hoạt động'),
(gen_random_uuid(), 1, 'EntityStatus', 3, 'DELETED', 'Đã xóa');

-- DataType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'DataType', 1, 'FLOAT', 'Số thực 32-bit'),
(gen_random_uuid(), 1, 'DataType', 2, 'DOUBLE', 'Số thực 64-bit'),
(gen_random_uuid(), 1, 'DataType', 3, 'INT', 'Số nguyên 32-bit'),
(gen_random_uuid(), 1, 'DataType', 4, 'LONG', 'Số nguyên 64-bit'),
(gen_random_uuid(), 1, 'DataType', 5, 'BOOL', 'Boolean'),
(gen_random_uuid(), 1, 'DataType', 6, 'STRING', 'Chuỗi ký tự'),
(gen_random_uuid(), 1, 'DataType', 7, 'DATETIME', 'Ngày giờ');

-- AssetType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'AssetType', 1, 'IMAGE', 'Hình ảnh'),
(gen_random_uuid(), 1, 'AssetType', 2, 'ICON', 'Icon'),
(gen_random_uuid(), 1, 'AssetType', 3, 'ANIMATION', 'Animation');

-- ProtocolType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'ProtocolType', 1, 'ANALOG', 'Analog input'),
(gen_random_uuid(), 1, 'ProtocolType', 2, 'MODBUS_RTU', 'Modbus RTU'),
(gen_random_uuid(), 1, 'ProtocolType', 3, 'MODBUS_TCP', 'Modbus TCP'),
(gen_random_uuid(), 1, 'ProtocolType', 4, 'OPC_UA', 'OPC UA'),
(gen_random_uuid(), 1, 'ProtocolType', 5, 'MQTT', 'MQTT');

-- TransformType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'TransformType', 1, 'NONE', 'Không transform'),
(gen_random_uuid(), 1, 'TransformType', 2, 'SCALE', 'Scale và offset'),
(gen_random_uuid(), 1, 'TransformType', 3, 'COMBINE_INT32', 'Ghép 2 registers thành INT32'),
(gen_random_uuid(), 1, 'TransformType', 4, 'COMBINE_FLOAT', 'Ghép 2 registers thành FLOAT32'),
(gen_random_uuid(), 1, 'TransformType', 5, 'FORMULA', 'Custom formula'),
(gen_random_uuid(), 1, 'TransformType', 6, 'BIT_EXTRACT', 'Lấy bit cụ thể');

-- ConditionType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'ConditionType', 1, 'EQ', 'Bằng'),
(gen_random_uuid(), 1, 'ConditionType', 2, 'NEQ', 'Khác'),
(gen_random_uuid(), 1, 'ConditionType', 3, 'GT', 'Lớn hơn'),
(gen_random_uuid(), 1, 'ConditionType', 4, 'GTE', 'Lớn hơn hoặc bằng'),
(gen_random_uuid(), 1, 'ConditionType', 5, 'LT', 'Nhỏ hơn'),
(gen_random_uuid(), 1, 'ConditionType', 6, 'LTE', 'Nhỏ hơn hoặc bằng'),
(gen_random_uuid(), 1, 'ConditionType', 7, 'BETWEEN', 'Trong khoảng'),
(gen_random_uuid(), 1, 'ConditionType', 8, 'NOT_BETWEEN', 'Ngoài khoảng'),
(gen_random_uuid(), 1, 'ConditionType', 9, 'BIT_ON', 'Bit bật'),
(gen_random_uuid(), 1, 'ConditionType', 10, 'BIT_OFF', 'Bit tắt'),
(gen_random_uuid(), 1, 'ConditionType', 11, 'IN', 'Trong danh sách'),
(gen_random_uuid(), 1, 'ConditionType', 12, 'NOT_IN', 'Ngoài danh sách'),
(gen_random_uuid(), 1, 'ConditionType', 13, 'LIKE', 'Giống'),
(gen_random_uuid(), 1, 'ConditionType', 14, 'NOT_LIKE', 'Không giống'),
(gen_random_uuid(), 1, 'ConditionType', 15, 'IS_NULL', 'Null'),
(gen_random_uuid(), 1, 'ConditionType', 16, 'IS_NOT_NULL', 'Không null');

-- ActionType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'ActionType', 1, 'CREATE_EVENT', 'Tạo sự kiện'),
(gen_random_uuid(), 1, 'ActionType', 2, 'CHANGE_STATUS', 'Đổi trạng thái'),
(gen_random_uuid(), 1, 'ActionType', 3, 'CALL_API', 'Gọi API'),
(gen_random_uuid(), 1, 'ActionType', 4, 'SEND_NOTIFICATION', 'Gửi thông báo'),
(gen_random_uuid(), 1, 'ActionType', 5, 'EXECUTE_COMMAND', 'Thực thi lệnh');

-- CommandType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'CommandType', 1, 'MODBUS_WRITE', 'Ghi Modbus'),
(gen_random_uuid(), 1, 'CommandType', 2, 'DIGITAL_OUTPUT', 'Digital Output'),
(gen_random_uuid(), 1, 'CommandType', 3, 'MQTT_PUBLISH', 'MQTT Publish'),
(gen_random_uuid(), 1, 'CommandType', 4, 'SYSTEM_CMD', 'Lệnh hệ thống');

-- CommandStatus
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'CommandStatus', 1, 'PENDING', 'Đang chờ'),
(gen_random_uuid(), 1, 'CommandStatus', 2, 'SENT', 'Đã gửi'),
(gen_random_uuid(), 1, 'CommandStatus', 3, 'SUCCESS', 'Thành công'),
(gen_random_uuid(), 1, 'CommandStatus', 4, 'FAILED', 'Thất bại'),
(gen_random_uuid(), 1, 'CommandStatus', 5, 'TIMEOUT', 'Hết thời gian');

-- EventSeverity
INSERT INTO master_constant_type (id, status, category, code, name, note, color) VALUES
(gen_random_uuid(), 1, 'EventSeverity', 1, 'INFO', 'Thông tin', '#2196F3'),
(gen_random_uuid(), 1, 'EventSeverity', 2, 'WARNING', 'Cảnh báo', '#FF9800'),
(gen_random_uuid(), 1, 'EventSeverity', 3, 'ALARM', 'Báo động', '#F44336'),
(gen_random_uuid(), 1, 'EventSeverity', 4, 'CRITICAL', 'Nghiêm trọng', '#9C27B0');

-- EventStatus
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'EventStatus', 1, 'ACTIVE', 'Đang hoạt động'),
(gen_random_uuid(), 1, 'EventStatus', 2, 'ACKNOWLEDGED', 'Đã xác nhận'),
(gen_random_uuid(), 1, 'EventStatus', 3, 'RESOLVED', 'Đã xử lý');

-- MachineStatus
INSERT INTO master_constant_type (id, status, category, code, name, note, color) VALUES
(gen_random_uuid(), 1, 'MachineStatus', 1, 'RUNNING', 'Đang chạy', '#4CAF50'),
(gen_random_uuid(), 1, 'MachineStatus', 2, 'STOP', 'Dừng', '#9E9E9E'),
(gen_random_uuid(), 1, 'MachineStatus', 3, 'ERROR', 'Lỗi', '#F44336'),
(gen_random_uuid(), 1, 'MachineStatus', 4, 'SYSTEM_STATUS', 'Trạng thái hệ thống', '#2196F3');
